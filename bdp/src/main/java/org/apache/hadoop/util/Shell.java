package org.apache.hadoop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

@InterfaceAudience.LimitedPrivate({"HDFS", "MapReduce"})
@InterfaceStability.Unstable
public abstract class Shell
{
  public static final Log LOG = LogFactory.getLog(Shell.class);

  private static boolean IS_JAVA7_OR_ABOVE = System.getProperty("java.version").substring(0, 3).compareTo("1.7") >= 0;
  public static final String USER_NAME_COMMAND = "whoami";
  public static final Object WindowsProcessLaunchLock = new Object();
  public static final String SET_PERMISSION_COMMAND = "chmod";
  public static final String SET_OWNER_COMMAND = "chown";
  public static final String SET_GROUP_COMMAND = "chgrp";
  public static final String LINK_COMMAND = "ln";
  public static final String READ_LINK_COMMAND = "readlink";
  protected long timeOutInterval = 0L;
  private AtomicBoolean timedOut;
  private static String HADOOP_HOME_DIR = checkHadoopHome();
  

  public static final boolean WINDOWS = System.getProperty("os.name").startsWith("Windows");

  public static final boolean LINUX = System.getProperty("os.name").startsWith("Linux");

  public static final String WINUTILS = getWinUtilsPath();

  public static final boolean isSetsidAvailable = isSetsidSupported();

  public static final String TOKEN_SEPARATOR_REGEX = WINDOWS ? "[|\n\r]" : "[ \t\n\r\f]";
  private long interval;
  private long lastTime;
  private Map<String, String> environment;
  private File dir;
  private Process process;
  private int exitCode;
  private volatile AtomicBoolean completed;

  public static boolean isJava7OrAbove()
  {
    return IS_JAVA7_OR_ABOVE;
  }

  public static String[] getGroupsCommand()
  {
    return new String[] { "bash", "-c", (String) (WINDOWS ? new String[] { "cmd", "/c", "groups" } : "groups") };
  }

  public static String[] getGroupsForUserCommand(String user)
  {
    return new String[] { "bash", "-c", (String) (WINDOWS ? new String[] { WINUTILS, "groups", "-F", new StringBuilder().append("\"").append(user).append("\"").toString() } : new StringBuilder().append("id -Gn ").append(user).toString()) };
  }

  public static String[] getUsersForNetgroupCommand(String netgroup)
  {
    return new String[] { "bash", "-c", (String) (WINDOWS ? new String[] { "cmd", "/c", new StringBuilder().append("getent netgroup ").append(netgroup).toString() } : new StringBuilder().append("getent netgroup ").append(netgroup).toString()) };
  }

  public static String[] getGetPermissionCommand()
  {
    return new String[] { "/bin/ls", (String) (WINDOWS ? new String[] { WINUTILS, "ls", "-F" } : "-ld") };
  }

  public static String[] getSetPermissionCommand(String perm, boolean recursive)
  {
    if (recursive) {
      return new String[] { "chmod", "-R", (String) (WINDOWS ? new String[] { WINUTILS, "chmod", "-R", perm } : perm) };
    }

    return new String[] { "chmod", WINDOWS ? new String[] { WINUTILS, "chmod", perm }.toString() : perm };
  }

  public static String[] getSetPermissionCommand(String perm, boolean recursive, String file)
  {
    String[] baseCmd = getSetPermissionCommand(perm, recursive);
    String[] cmdWithFile = (String[])Arrays.copyOf(baseCmd, baseCmd.length + 1);
    cmdWithFile[(cmdWithFile.length - 1)] = file;
    return cmdWithFile;
  }

  public static String[] getSetOwnerCommand(String owner)
  {
    return new String[] { "chown", (String) (WINDOWS ? new String[] { WINUTILS, "chown", new StringBuilder().append("\"").append(owner).append("\"").toString() } : owner) };
  }

  public static String[] getSymlinkCommand(String target, String link)
  {
    return new String[] { "ln", "-s", target, (String) (WINDOWS ? new String[] { WINUTILS, "symlink", link, target } : link) };
  }

  public static String[] getReadlinkCommand(String link)
  {
    return new String[] { "readlink", (String) (WINDOWS ? new String[] { WINUTILS, "readlink", link } : link) };
  }

  public static String[] getCheckProcessIsAliveCommand(String pid)
  {
    return new String[] { "kill", "-0", (String) (isSetsidAvailable ? new StringBuilder().append("-").append(pid).toString() : WINDOWS ? new String[] { WINUTILS, "task", "isAlive", pid } : pid) };
  }

  public static String[] getSignalKillCommand(int code, String pid)
  {
    return new String[] { "kill", new StringBuilder().append("-").append(code).toString(), (String) (isSetsidAvailable ? new StringBuilder().append("-").append(pid).toString() : WINDOWS ? new String[] { WINUTILS, "task", "kill", pid } : pid) };
  }

  public static String getEnvironmentVariableRegex()
  {
    return WINDOWS ? "%([A-Za-z_][A-Za-z0-9_]*?)%" : "\\$([A-Za-z_][A-Za-z0-9_]*)";
  }

  public static File appendScriptExtension(File parent, String basename)
  {
    return new File(parent, appendScriptExtension(basename));
  }

  public static String appendScriptExtension(String basename)
  {
    return new StringBuilder().append(basename).append(WINDOWS ? ".cmd" : ".sh").toString();
  }

  public static String[] getRunScriptCommand(File script)
  {
    String absolutePath = script.getAbsolutePath();
    return new String[] { "/bin/bash", (String) (WINDOWS ? new String[] { "cmd", "/c", absolutePath } : absolutePath) };
  }

  private static String checkHadoopHome()
  {
    String home = System.getProperty("hadoop.home.dir");

    if (home == null) {
      home = System.getenv("HADOOP_HOME");
    }

    try
    {
      if (home == null) {
        throw new IOException("HADOOP_HOME or hadoop.home.dir are not set.");
      }

      if ((home.startsWith("\"")) && (home.endsWith("\""))) {
        home = home.substring(1, home.length() - 1);
      }

      File homedir = new File(home);
      if ((!homedir.isAbsolute()) || (!homedir.exists()) || (!homedir.isDirectory())) {
        throw new IOException(new StringBuilder().append("Hadoop home directory ").append(homedir).append(" does not exist, is not a directory, or is not an absolute path.").toString());
      }

      home = homedir.getCanonicalPath();
    }
    catch (IOException ioe) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Failed to detect a valid hadoop home directory", ioe);
      }
      home = null;
    }

    return home;
  }

  public static final String getHadoopHome()
    throws IOException
  {
    if (HADOOP_HOME_DIR == null) {
      throw new IOException("Misconfigured HADOOP_HOME cannot be referenced.");
    }

    return HADOOP_HOME_DIR;
  }

  public static final String getQualifiedBinPath(String executable)
    throws IOException
  {
    String fullExeName = new StringBuilder().append(HADOOP_HOME_DIR).append(File.separator).append("bin").append(File.separator).append(executable).toString();
   //System.out.println("fullExeName="+fullExeName);
    File exeFile = new File(fullExeName);
    if (!exeFile.exists()) {
      throw new IOException(new StringBuilder().append("Could not locate executable ").append(fullExeName).append(" in the Hadoop binaries.").toString());
    }

    return exeFile.getCanonicalPath();
  }

  public static final String getWinUtilsPath()
  {
    String winUtilsPath = null;
    try
    {
      if (WINDOWS)
        winUtilsPath = getQualifiedBinPath("winutils.exe");
    }
    catch (IOException ioe) {
      LOG.error("Failed to locate the winutils binary in the hadoop binary path", ioe);
    }

    return winUtilsPath;
  }

  private static boolean isSetsidSupported()
  {
    if (WINDOWS) {
      return false;
    }
    ShellCommandExecutor shexec = null;
    boolean setsidSupported = true;
    try {
      String[] args = { "setsid", "bash", "-c", "echo $$" };
      shexec = new ShellCommandExecutor(args);
      shexec.execute();
    } catch (IOException ioe) {
      LOG.debug("setsid is not available on this machine. So not using it.");
      setsidSupported = false;
    } finally {
      if (LOG.isDebugEnabled()) {
        LOG.debug(new StringBuilder().append("setsid exited with exit code ").append(shexec != null ? Integer.valueOf(shexec.getExitCode()) : "(null executor)").toString());
      }
    }

    return setsidSupported;
  }

  public Shell()
  {
    this(0L);
  }

  public Shell(long interval)
  {
    this.interval = interval;
    this.lastTime = (interval < 0L ? 0L : -interval);
  }

  protected void setEnvironment(Map<String, String> env)
  {
    this.environment = env;
  }

  protected void setWorkingDirectory(File dir)
  {
    this.dir = dir;
  }

  protected void run() throws IOException
  {
    if (this.lastTime + this.interval > Time.now())
      return;
    this.exitCode = 0;
    runCommand();
  }

  private void runCommand() throws IOException
  {
    ProcessBuilder builder = new ProcessBuilder(getExecString());
    Timer timeOutTimer = null;
    ShellTimeoutTimerTask timeoutTimerTask = null;
    this.timedOut = new AtomicBoolean(false);
    this.completed = new AtomicBoolean(false);

    if (this.environment != null) {
      builder.environment().putAll(this.environment);
    }
    if (this.dir != null) {
      builder.directory(this.dir);
    }

    if (WINDOWS)
      synchronized (WindowsProcessLaunchLock)
      {
        this.process = builder.start();
      }
    else {
      this.process = builder.start();
    }

    if (this.timeOutInterval > 0L) {
      timeOutTimer = new Timer("Shell command timeout");
      timeoutTimerTask = new ShellTimeoutTimerTask(this);

      timeOutTimer.schedule(timeoutTimerTask, this.timeOutInterval);
    }
    final BufferedReader errReader = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));

    BufferedReader inReader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));

    final StringBuffer errMsg = new StringBuffer();

    Thread errThread = new Thread()
    {
      public void run() {
        try {
          String line = errReader.readLine();
          while ((line != null) && (!isInterrupted())) {
            errMsg.append(line);
            errMsg.append(System.getProperty("line.separator"));
            line = errReader.readLine();
          }
        } catch (IOException ioe) {
          Shell.LOG.warn("Error reading the error stream", ioe);
        }
      }
    };
    try {
      errThread.start(); } catch (IllegalStateException ise) {
    }
    try {
      parseExecResult(inReader);

      String line = inReader.readLine();
      while (line != null) {
        line = inReader.readLine();
      }

      this.exitCode = this.process.waitFor();
      try
      {
        errThread.join();
      } catch (InterruptedException ie) {
        LOG.warn("Interrupted while reading the error stream", ie);
      }
      this.completed.set(true);

      if (this.exitCode != 0)
        throw new ExitCodeException(this.exitCode, errMsg.toString());
    }
    catch (InterruptedException ie) {
      throw new IOException(ie.toString());
    } finally {
      if (timeOutTimer != null) {
        timeOutTimer.cancel();
      }
      try
      {
        inReader.close();
      } catch (IOException ioe) {
        LOG.warn("Error while closing the input stream", ioe);
      }
      try {
        if (!this.completed.get()) {
          errThread.interrupt();
          errThread.join();
        }
      } catch (InterruptedException ie) {
        LOG.warn("Interrupted while joining errThread");
      }
      try {
        errReader.close();
      } catch (IOException ioe) {
        LOG.warn("Error while closing the error stream", ioe);
      }
      this.process.destroy();
      this.lastTime = Time.now();
    }
  }

  protected abstract String[] getExecString();

  protected abstract void parseExecResult(BufferedReader paramBufferedReader)
    throws IOException;

  public Process getProcess()
  {
    return this.process;
  }

  public int getExitCode()
  {
    return this.exitCode;
  }

  public boolean isTimedOut()
  {
    return this.timedOut.get();
  }

  private void setTimedOut()
  {
    this.timedOut.set(true);
  }

  public static String execCommand(String[] cmd)
    throws IOException
  {
    return execCommand(null, cmd, 0L);
  }

  public static String execCommand(Map<String, String> env, String[] cmd, long timeout)
    throws IOException
  {
    ShellCommandExecutor exec = new ShellCommandExecutor(cmd, null, env, timeout);

    exec.execute();
    return exec.getOutput();
  }

  public static String execCommand(Map<String, String> env, String[] cmd)
    throws IOException
  {
    return execCommand(env, cmd, 0L);
  }

  private static class ShellTimeoutTimerTask extends TimerTask
  {
    private Shell shell;

    public ShellTimeoutTimerTask(Shell shell)
    {
      this.shell = shell;
    }

    public void run()
    {
      Process p = this.shell.getProcess();
      try {
        p.exitValue();
      }
      catch (Exception e)
      {
        if ((p != null) && (!this.shell.completed.get())) {
          this.shell.setTimedOut();
          p.destroy();
        }
      }
    }
  }

  public static class ShellCommandExecutor extends Shell
  {
    private String[] command;
    private StringBuffer output;

    public ShellCommandExecutor(String[] execString)
    {
      this(execString, null);
    }

    public ShellCommandExecutor(String[] execString, File dir) {
      this(execString, dir, null);
    }

    public ShellCommandExecutor(String[] execString, File dir, Map<String, String> env)
    {
      this(execString, dir, env, 0L);
    }

    public ShellCommandExecutor(String[] execString, File dir, Map<String, String> env, long timeout)
    {
      this.command = ((String[])execString.clone());
      if (dir != null) {
        setWorkingDirectory(dir);
      }
      if (env != null) {
        setEnvironment(env);
      }
      this.timeOutInterval = timeout;
    }

    public void execute()
      throws IOException
    {
      run();
    }

    public String[] getExecString()
    {
      return this.command;
    }

    protected void parseExecResult(BufferedReader lines) throws IOException
    {
      this.output = new StringBuffer();
      char[] buf = new char[512];
      int nRead;
      while ((nRead = lines.read(buf, 0, buf.length)) > 0)
        this.output.append(buf, 0, nRead);
    }

    public String getOutput()
    {
      return this.output == null ? "" : this.output.toString();
    }

    public String toString()
    {
      StringBuilder builder = new StringBuilder();
      String[] args = getExecString();
      for (String s : args) {
        if (s.indexOf(' ') >= 0)
          builder.append('"').append(s).append('"');
        else {
          builder.append(s);
        }
        builder.append(' ');
      }
      return builder.toString();
    }
  }

  public static class ExitCodeException extends IOException
  {
    int exitCode;

    public ExitCodeException(int exitCode, String message)
    {
      super();
      this.exitCode = exitCode;
    }

    public int getExitCode() {
      return this.exitCode;
    }
  }
}
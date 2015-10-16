package appsoft.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import appsoft.db.hb.RowKey;
import appsoft.db.hb.core.Nullable;
import appsoft.util.exceptions.BDException;


/**
 * Client CommonUtil.
 * 
 * @author xinzhi
 * */
public class CommonUtil {

    /**
     * Check boolean is NOT false.
     * */
    public static void check(boolean bool) {
        if (bool == false) {
            throw new BDException("bool is false.");
        }
    }

    /**
     * Check object is NOT null.
     * */
    public static void checkNull(Object obj) {
        if (obj == null) {
            throw new BDException("obj  is null.");
        }
    }

    /**
     * Check for string is NOT null or empty string.
     * */
    public static void checkEmptyString(String str) {
        if (StrUtil.isEmpty(str)) {
            throw new BDException("str is null or empty.");
        }
    }

    /**
     * Check the value's length.
     * */
    public static void checkLength(byte[] values, int length) {
        CommonUtil.checkNull(values);

        if (values.length != length) {
            throw new BDException("checkLength error. values.length="
                    + values.length + " length=" + length);
        }
    }

    /**
     * Check string's length.
     * */
    public static void checkLength(String str, int length) {
        CommonUtil.checkNull(str);

        if (str.length() != length) {
            throw new BDException("checkLength error. str=" + str
                    + " length=" + length);
        }
    }

    /**
     * Check rowKey.
     * 
     * <pre>
     * rowKey is not null.
     * the result of rowKey's toBytes is not null.
     * </pre>
     * 
     * */
    public static void checkRowKey(RowKey rowKey) {
        checkNull(rowKey);
        if (rowKey.toBytes() == null) {
            throw new BDException("rowkey bytes is null. rowKey = "
                    + rowKey);
        }
    }

    /**
     * Check rowKey list.
     * 
     * <pre>
     * rowKeyList is not null or empty.
     * each row key in list is valid.
     * </pre>
     * 
     * */
    public static void checkRowKeyList(List<RowKey> rowKeyList) {
        CommonUtil.checkNull(rowKeyList);
        CommonUtil.check(!rowKeyList.isEmpty());
        for (RowKey rowKey : rowKeyList) {
            CommonUtil.checkRowKey(rowKey);
        }
    }



    /**
     * Check for 2 objects have identity type.
     * */
    public static void checkIdentityType(Object one, Object other) {
        checkNull(one);
        checkNull(other);

        if (one.getClass() != other.getClass()) {
            throw new BDException("not same type. one = " + one
                    + " other = " + other);
        }
    }

    /**
     * Check for 2 objects are equal.
     * */
    public static void checkEquals(Object one, Object other) {
        if (one == other) {
            return;
        }

        if (one == null || other == null) {
            throw new BDException("null object. one = " + one
                    + " other = " + other);
        }
        if (!one.equals(other)) {
            throw new BDException("not equal object. one = " + one
                    + " other = " + other);
        }
    }

    /**
     * Close Closeable.
     * */
    public static void close(@Nullable Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            throw new BDException("close closeable exception.", e);
        }
    }

    private CommonUtil() {
    }
}

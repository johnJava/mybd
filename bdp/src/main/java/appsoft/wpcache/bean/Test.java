package appsoft.wpcache.bean;

import java.util.Random;

/**
 * 测试缓存实体专用
 * @author caoyang
 *
 */
public class Test {
    public static void main(String[] args) {
    	Random random = new Random();
        Random random2 = new Random(100);//指定种子数100
       System.out.println( random.nextFloat());
    }

    	
}

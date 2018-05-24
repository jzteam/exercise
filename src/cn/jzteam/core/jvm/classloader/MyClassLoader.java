package cn.jzteam.core.jvm.classloader;

import com.alibaba.fastjson.JSON;

import java.io.*;

public class MyClassLoader extends ClassLoader {
    private String classpath;

    public MyClassLoader(String classpath) {
        this.classpath = classpath;
    }

    /**
     * 真正地加载自己负责的类
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] classByte = getDate(name);

            if (classByte == null) {

            } else {
                //defineClass方法将字节码转化为类
                return defineClass(name, classByte, 0, classByte.length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    //返回类的字节码
    private byte[] getDate(String className) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        String path = classpath + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
        try {
            in = new FileInputStream(path);
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            in.close();
            out.close();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        final MyClassLoader cl = new MyClassLoader("/Users/oker/jzteam/output/java/");
        final Class<?> clazz = cl.loadClass("cn.jzteam.barber.form.UserForm");
        System.out.println("className:"+clazz.getSimpleName());
        System.out.println(JSON.toJSONString(clazz.newInstance()));


    }


}

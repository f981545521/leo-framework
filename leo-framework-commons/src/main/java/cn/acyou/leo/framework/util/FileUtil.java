package cn.acyou.leo.framework.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 文件操作工具
 *
 * <pre>
 *         File file = new File("D:\\ToUpload\\G.E.M.邓紫棋 - A.I.N.Y..mp3");
 *         System.out.println(FileUtil.getName(file.getAbsolutePath()));//G.E.M.邓紫棋 - A.I.N.Y..mp3
 *         System.out.println(FileUtil.getBaseName(file.getAbsolutePath()));//G.E.M.邓紫棋 - A.I.N.Y.
 *         System.out.println(FileUtil.getExtension(file.getAbsolutePath()));//mp3
 *         System.out.println(FileUtil.getFullPath(file.getAbsolutePath()));//D:\ToUpload\
 *         System.out.println(FileUtil.getFullPathNoEndSeparator(file.getAbsolutePath()));//D:\ToUpload
 *
 *         System.out.println(file.getAbsolutePath());//D:\ToUpload\G.E.M.邓紫棋 - A.I.N.Y..mp3
 *         System.out.println(file.getName());//G.E.M.邓紫棋 - A.I.N.Y..mp3
 *         System.out.println(file.getPath());//D:\ToUpload\G.E.M.邓紫棋 - A.I.N.Y..mp3
 *         System.out.println(file.getParentFile().getAbsolutePath());//D:\ToUpload
 *         System.out.println(file.getCanonicalFile().getAbsolutePath());//D:\ToUpload\G.E.M.邓紫棋 - A.I.N.Y..mp3
 *         System.out.println(file.getParent());//D:\ToUpload
 *         System.out.println(file.getFreeSpace());//121102061568
 *         System.out.println(file.getTotalSpace());//214769332224
 *         System.out.println(FileUtil.getTypeByStream(file));//null  == 这个方法不太好用
 *         System.out.println(FileUtil.extName(file));//mp3
 *         System.out.println(FileUtil.isImage(FileUtil.extName(file)));//false
 *         System.out.println(FileUtil.nameWithoutExtend(file));//G.E.M.邓紫棋 - A.I.N.Y.
 *
 *         System.out.println(FileUtil.getTmpDir());//C:\Users\1\AppData\Local\Temp
 *         System.out.println(FileUtil.getUserHomeDir());//C:\Users\1
 *         System.out.println(FileUtil.createTempDir("LEO-1"));//C:\Users\1\AppData\Local\Temp\LEO-1
 *         System.out.println(FileUtil.createTempDirWithPrefix("LEO-1"));//C:\Users\1\AppData\Local\Temp\LEO-1283923104174592146
 *         System.out.println(FileUtil.exists(file.getPath()));//true
 *         System.out.println(FileUtil.notExists(file.getPath()));//false
 *         System.out.println(FileUtil.createTempFile("", ".txt"));//C:\Users\1\AppData\Local\Temp\8893718731893761028.txt
 *         System.out.println(FileUtil.createTempFile("frames", "", ".jpg"));//C:\Users\1\AppData\Local\Temp\frames\6163292665980665645.jpg
 *         System.out.println(FileUtil.createTempFileFullName("12314.txt").getAbsolutePath());//C:\Users\1\AppData\Local\Temp\12314.txt
 *         System.out.println(FileUtil.createTempFileFullName("222", "12314.txt").getAbsolutePath());//C:\Users\1\AppData\Local\Temp\222\12314.txt
 * </pre>
 *
 * @author youfang
 * @version [1.0.0, 2020/7/29]
 **/
@Slf4j
public class FileUtil {
    /**
     * 文件分隔符
     */
    public static final String SEPARATOR = File.separator;
    /**
     * 临时文件目录
     */
    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    /**
     * 用户文件目录
     */
    public static final String USER_HOME_DIR = System.getProperty("user.home");

    /**
     * 默认 NOT_FOUND
     */
    private static final int NOT_FOUND = -1;
    /**
     * 扩展名标记
     */
    public static final char EXTENSION_SEPARATOR = '.';
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * 获取临时文件目录
     *
     * @return 临时文件目录
     * @since 4.0.6
     */
    public static File getTmpDir() {
        return new File(TEMP_DIR);
    }


    /**
     * 获取用户目录
     *
     * @return 用户目录
     * @since 4.0.6
     */
    public static File getUserHomeDir() {
        return new File(USER_HOME_DIR);
    }

    /**
     * 创建临时文件的目录
     *
     *  <pre>
     *  FileUtil.createTempDir("LEO-1")
     *  结果是: //C:\Users\1\AppData\Local\Temp\LEO-1
     *  </pre>
     * @param subDir 子目录名称
     * @return 临时目录下的目录
     */
    public static File createTempDir(String subDir) {
        File dirFile = new File(TEMP_DIR, subDir);
        if (!dirFile.exists()) {
            log.info("创建临时目录：{} {}", dirFile.getAbsolutePath(), dirFile.mkdirs() ? "成功" : "失败");
        }
        return dirFile;
    }

    /**
     * 创建临时文件的目录(使用前缀)
     *
     *  <pre>
     *  FileUtil.createTempDirWithPrefix("LEO-1")
     *  结果是: //C:\Users\1\AppData\Local\Temp\LEO-1283923104174592146
     *  </pre>
     *
     * @param prefix 前缀名称
     * @return 临时目录下的目录
     */
    public static File createTempDirWithPrefix(String prefix) throws Exception {
        Path tempDirectory = Files.createTempDirectory(prefix);
        return tempDirectory.toFile();
    }

    /**
     * 递归删除
     *
     * <p>1. 删除文件</p>
     * <p>2. 删除文件夹(包含文件)</p>
     *
     * @param file 文件
     * @return boolean
     */
    public static boolean deleteRecursively(File file) {
        return FileSystemUtils.deleteRecursively(file);
    }

    /**
     * 递归拷贝 文件/文件夹
     *
     * <p>1. 拷贝文件</p>
     * <p>2. 拷贝文件夹(包含文件)</p>
     *
     * @param src 源文件/文件夹
     * @param dest 目标文件/文件夹
     */
    public static void copyRecursively(File src, File dest) throws Exception {
        FileSystemUtils.copyRecursively(src, dest);
    }

    /**
     * 在文件夹下创建临时文件
     *
     * <pre>
     *     示例：
     *     FileUtil.createTempFile("", ".txt")
     *     := C:\Users\1\AppData\Local\Temp\636785312213824955.txt
     *     FileUtil.createTempFile("video", ".txt")
     *     := C:\Users\1\AppData\Local\Temp\video636785312213824955.txt
     * </pre>
     * @param prefix 文件前缀
     * @param extendName 文件扩展名
     * @return 文件
     * @throws IOException 异常
     */
    public static File createTempFile(String prefix, String extendName) throws IOException {
        return Files.createTempFile(prefix, extendName).toFile();
    }

    /**
     * 在文件夹下创建临时文件
     *
     *
     * <pre>
     *     示例：
     *     FileUtil.createTempFile("frames", "", ".jpg")
     *     := C:\Users\1\AppData\Local\Temp\frames\7019713496942790276.jpg
     *     FileUtil.createTempFile("frames", "video", ".jpg")
     *     := C:\Users\1\AppData\Local\Temp\frames\video7019713496942790276.jpg
     * </pre>
     *
     * @param subDir 文件夹
     * @param prefix 文件前缀
     * @param extendName 文件扩展名
     * @return 文件
     * @throws IOException 异常
     */
    public static File createTempFile(String subDir, String prefix, String extendName) throws IOException {
        File tempDir = createTempDir(subDir);
        return Files.createTempFile(tempDir.toPath(), prefix, extendName).toFile();
    }

    /**
     * 使用文件全名 创建临时文件
     *
     * <pre>
     *     示例：
     *     FileUtil.createTempFileFullName("12314.txt")
     *     := C:\Users\1\AppData\Local\Temp\12314.txt
     * </pre>
     *
     * @param fullName 全名
     * @return {@link File}
     */
    public static File createTempFileFullName(String fullName) {
        return new File(getTmpDir(), fullName);
    }

    /**
     * 使用文件全名 创建临时文件
     * <pre>
     *     示例：
     *     FileUtil.createTempFileFullName("222", "12314.txt")
     *     := C:\Users\1\AppData\Local\Temp\222\12314.txt
     * </pre>
     * @param fullName 全名
     * @return {@link File}
     */
    public static File createTempFileFullName(String subDir, String fullName) {
        return new File(createTempDir(subDir), fullName);
    }

    public static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    public static boolean notExists(String path) {
        return Files.notExists(Paths.get(path));
    }

    public static Path copy(String source, String target) throws Exception {
        return Files.copy(Paths.get(source), Paths.get(target));
    }


    private static final Map<String, String> FILE_TYPE_MAP;

    /**
     * 未知类型
     */
    public static final String UNKNOWN = "UNKNOWN";

    static {
        FILE_TYPE_MAP = new ConcurrentSkipListMap<>((s1, s2) -> {
            int len1 = s1.length();
            int len2 = s2.length();
            if (len1 == len2) {
                return s1.compareTo(s2);
            } else {
                return len2 - len1;
            }
        });

        FILE_TYPE_MAP.put("ffd8ff", "jpg"); // JPEG (jpg)
        FILE_TYPE_MAP.put("89504e47", "png"); // PNG (png)
        FILE_TYPE_MAP.put("4749463837", "gif"); // GIF (gif)
        FILE_TYPE_MAP.put("4749463839", "gif"); // GIF (gif)
        FILE_TYPE_MAP.put("49492a00227105008037", "tif"); // TIFF (tif)
        FILE_TYPE_MAP.put("424d228c010000000000", "bmp"); // 16色位图(bmp)
        FILE_TYPE_MAP.put("424d8240090000000000", "bmp"); // 24色位图(bmp)
        FILE_TYPE_MAP.put("424d8e1b030000000000", "bmp"); // 256色位图(bmp)
        FILE_TYPE_MAP.put("41433130313500000000", "dwg"); // CAD (dwg)
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); // Rich Text Format (rtf)
        FILE_TYPE_MAP.put("38425053000100000000", "psd"); // Photoshop (psd)
        FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml"); // Email [Outlook Express 6] (eml)
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
        FILE_TYPE_MAP.put("255044462d312e", "pdf"); // Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); // rmvb/rm相同
        FILE_TYPE_MAP.put("464c5601050000000900", "flv"); // flv与f4v相同
        FILE_TYPE_MAP.put("0000001C66747970", "mp4");
        FILE_TYPE_MAP.put("00000020667479706", "mp4");
        FILE_TYPE_MAP.put("00000018667479706D70", "mp4");
        FILE_TYPE_MAP.put("49443303000000002176", "mp3");
        FILE_TYPE_MAP.put("000001ba210001000180", "mpg"); //
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); // wmv与asf相同
        FILE_TYPE_MAP.put("52494646e27807005741", "wav"); // Wave (wav)
        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");
        FILE_TYPE_MAP.put("4d546864000000060001", "mid"); // MIDI (mid)
        FILE_TYPE_MAP.put("526172211a0700cf9073", "rar"); // WinRAR
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
        FILE_TYPE_MAP.put("504B03040a0000000000", "jar");
        FILE_TYPE_MAP.put("504B0304140008000800", "jar");
        // MS Excel 注意：word、msi 和 excel的文件头一样
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10", "xls");
        FILE_TYPE_MAP.put("504B0304", "zip");
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe"); // 可执行文件
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp"); // jsp文件
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf"); // MF文件
        FILE_TYPE_MAP.put("7061636b616765207765", "java"); // java文件
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat"); // bat文件
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz"); // gz文件
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class"); // class文件
        FILE_TYPE_MAP.put("49545346030000006000", "chm"); // chm文件
        FILE_TYPE_MAP.put("04000000010000001300", "mxp"); // mxp文件
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");
        FILE_TYPE_MAP.put("6D6F6F76", "mov"); // Quicktime (mov)
        FILE_TYPE_MAP.put("FF575043", "wpd"); // WordPerfect (wpd)
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
        FILE_TYPE_MAP.put("2142444E", "pst"); // Outlook (pst)
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); // Quicken (qdf)
        FILE_TYPE_MAP.put("E3828596", "pwl"); // Windows Password (pwl)
        FILE_TYPE_MAP.put("2E7261FD", "ram"); // Real Audio (ram)
    }


    /**
     * 根据文件流的头部信息获得文件类型
     *
     * @param fileStreamHexHead 文件流头部16进制字符串
     * @return 文件类型，未找到为{@code null}
     */
    private static String getType(String fileStreamHexHead) {
        for (Map.Entry<String, String> fileTypeEntry : FILE_TYPE_MAP.entrySet()) {
            if (fileStreamHexHead.toLowerCase().startsWith(fileTypeEntry.getKey().toLowerCase())) {
                return fileTypeEntry.getValue();
            }
        }
        return null;
    }

    /**
     * 根据文件流的头部信息获得文件类型
     * <p>注意：<strong>有很多无法识别类型</strong></p>
     *
     * @param file 文件 {@link File}
     * @return 类型，文件的扩展名，未找到为{@code null}
     */
    public static String getTypeByStream(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            return getTypeByStream(in);
        }catch (Exception e){
            //ignore
        }finally {
            IOUtil.close(in);
        }
        return UNKNOWN;
    }


    /**
     * 通过路径获得文件类型
     *
     * @param path 路径，绝对路径或相对ClassPath的路径
     * @return 类型
     */
    public static String getTypeByStream(String path) {
        return getTypeByStream(new File(path));
    }

    /**
     * 根据文件流的头部信息获得文件类型
     *
     * @param in {@link InputStream}
     * @return 类型，文件的扩展名，未找到为{@code null}
     */
    public static String getTypeByStream(InputStream in) {
        try {
            return getType(IOUtil.readHex28Upper(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return UNKNOWN;
    }


    /**
     * 常用静态图片文件类型
     */
    private static final List<String> imageTypes = Lists.newArrayList("png", "jpg", "jpeg");

    /**
     * 获取文件扩展名，扩展名不带“.”
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String extName(File file) {
        if (null == file) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        return extName(file.getName());
    }

    /**
     * 获取文件扩展名，扩展名不带“.”
     *
     * @param fileName 文件名称
     * @return {@link String}
     */
    public static String extName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(StringUtils.DOT);
        if (index == -1) {
            return null;
        } else {
            return fileName.substring(index + 1);
        }
    }

    /**
     * 判断是否是图片格式
     *
     * @param extName 文件扩展名
     * @return boolean
     */
    public static boolean isImage(String extName) {
        if (StringUtils.isBlank(extName)) {
            return false;
        }
        return imageTypes.contains(extName.toLowerCase());
    }

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\ToUpload\\G.E.M.邓紫棋 - A.I.N.Y..mp3");
        System.out.println(FileUtil.getName(file.getAbsolutePath()));//G.E.M.邓紫棋 - A.I.N.Y..mp3
        System.out.println(FileUtil.getBaseName(file.getAbsolutePath()));//G.E.M.邓紫棋 - A.I.N.Y.
        System.out.println(FileUtil.getExtension(file.getAbsolutePath()));//mp3
        System.out.println(FileUtil.getFullPath(file.getAbsolutePath()));//D:\ToUpload\
        System.out.println(FileUtil.getFullPathNoEndSeparator(file.getAbsolutePath()));//D:\ToUpload
    }

    //===========================================

    /**
     * Gets the name minus the path from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The text after the last forward or backslash is returned.
     * <pre>
     * a/b/c.txt --&gt; c.txt
     * a.txt     --&gt; a.txt
     * a/b/c     --&gt; c
     * a/b/c/    --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists.
     * Null bytes inside string will be removed
     */
    public static String getName(final String filename) {
        if (filename == null) {
            return null;
        }
        failIfNullBytePresent(filename);
        final int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }


    /**
     * Gets the base name, minus the full path and extension, from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The text after the last forward or backslash and before the last dot is returned.
     * <pre>
     * a/b/c.txt --&gt; c
     * a.txt     --&gt; a
     * a/b/c     --&gt; c
     * a/b/c/    --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists. Null bytes inside string
     * will be removed
     */
    public static String getBaseName(final String filename) {
        return removeExtension(getName(filename));
    }

    /**
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt      --&gt; "txt"
     * a/b/c.jpg    --&gt; "jpg"
     * a/b.txt/c    --&gt; ""
     * a/b/c        --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists or {@code null}
     * if the filename is {@code null}.
     */
    public static String getExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfExtension(filename);
        if (index == NOT_FOUND) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before and
     * including the last forward or backslash.
     * <pre>
     * C:\a\b\c.txt --&gt; C:\a\b\
     * ~/a/b/c.txt  --&gt; ~/a/b/
     * a.txt        --&gt; ""
     * a/b/c        --&gt; a/b/
     * a/b/c/       --&gt; a/b/c/
     * C:           --&gt; C:
     * C:\          --&gt; C:\
     * ~            --&gt; ~/
     * ~/           --&gt; ~/
     * ~user        --&gt; ~user/
     * ~user/       --&gt; ~user/
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getFullPath(final String filename) {
        return doGetFullPath(filename, true);
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path,
     * and also excluding the final directory separator.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The method is entirely text based, and returns the text before the
     * last forward or backslash.
     * <pre>
     * C:\a\b\c.txt --&gt; C:\a\b
     * ~/a/b/c.txt  --&gt; ~/a/b
     * a.txt        --&gt; ""
     * a/b/c        --&gt; a/b
     * a/b/c/       --&gt; a/b/c
     * C:           --&gt; C:
     * C:\          --&gt; C:\
     * ~            --&gt; ~
     * ~/           --&gt; ~
     * ~user        --&gt; ~user
     * ~user/       --&gt; ~user
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getFullPathNoEndSeparator(final String filename) {
        return doGetFullPath(filename, false);
    }


    //-----------------------------------------------------------------------

    /**
     * Does the work of getting the path.
     *
     * @param filename         the filename
     * @param includeSeparator true to include the end separator
     * @return the path
     */
    private static String doGetFullPath(final String filename, final boolean includeSeparator) {
        if (filename == null) {
            return null;
        }
        final int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        if (prefix >= filename.length()) {
            if (includeSeparator) {
                return getPrefix(filename);  // add end slash if necessary
            } else {
                return filename;
            }
        }
        final int index = indexOfLastSeparator(filename);
        if (index < 0) {
            return filename.substring(0, prefix);
        }
        int end = index + (includeSeparator ? 1 : 0);
        if (end == 0) {
            end++;
        }
        return filename.substring(0, end);
    }

    private static String getPrefix(final String filename) {
        if (filename == null) {
            return null;
        }
        final int len = getPrefixLength(filename);
        if (len < 0) {
            return null;
        }
        if (len > filename.length()) {
            failIfNullBytePresent(filename + UNIX_SEPARATOR);
            return filename + UNIX_SEPARATOR;
        }
        final String path = filename.substring(0, len);
        failIfNullBytePresent(path);
        return path;
    }

    private static int getPrefixLength(final String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        final int len = filename.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = filename.charAt(0);
        if (ch0 == ':') {
            return NOT_FOUND;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2;  // return a length greater than the input
            }
            return isSeparator(ch0) ? 1 : 0;
        } else {
            if (ch0 == '~') {
                int posUnix = filename.indexOf(UNIX_SEPARATOR, 1);
                int posWin = filename.indexOf(WINDOWS_SEPARATOR, 1);
                if (posUnix == NOT_FOUND && posWin == NOT_FOUND) {
                    return len + 1;  // return a length greater than the input
                }
                posUnix = posUnix == NOT_FOUND ? posWin : posUnix;
                posWin = posWin == NOT_FOUND ? posUnix : posWin;
                return Math.min(posUnix, posWin) + 1;
            }
            final char ch1 = filename.charAt(1);
            if (ch1 == ':') {
                ch0 = Character.toUpperCase(ch0);
                if (ch0 >= 'A' && ch0 <= 'Z') {
                    if (len == 2 || isSeparator(filename.charAt(2)) == false) {
                        return 2;
                    }
                    return 3;
                } else if (ch0 == UNIX_SEPARATOR) {
                    return 1;
                }
                return NOT_FOUND;

            } else if (isSeparator(ch0) && isSeparator(ch1)) {
                int posUnix = filename.indexOf(UNIX_SEPARATOR, 2);
                int posWin = filename.indexOf(WINDOWS_SEPARATOR, 2);
                if (posUnix == NOT_FOUND && posWin == NOT_FOUND || posUnix == 2 || posWin == 2) {
                    return NOT_FOUND;
                }
                posUnix = posUnix == NOT_FOUND ? posWin : posUnix;
                posWin = posWin == NOT_FOUND ? posUnix : posWin;
                return Math.min(posUnix, posWin) + 1;
            } else {
                return isSeparator(ch0) ? 1 : 0;
            }
        }
    }

    private static boolean isSeparator(final char ch) {
        return ch == UNIX_SEPARATOR || ch == WINDOWS_SEPARATOR;
    }

    /**
     * Removes the extension from a filename.
     * <p>
     * This method returns the textual part of the filename before the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt    --&gt; foo
     * a\b\c.jpg  --&gt; a\b\c
     * a\b\c      --&gt; a\b\c
     * a.b\c      --&gt; a.b\c
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the filename minus the extension
     */
    private static String removeExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        failIfNullBytePresent(filename);

        final int index = indexOfExtension(filename);
        if (index == NOT_FOUND) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    private static int indexOfExtension(final String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        final int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        final int lastSeparator = indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
    }

    private static int indexOfLastSeparator(final String filename) {
        if (filename == null) {
            return NOT_FOUND;
        }
        final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * Check the input for null bytes, a sign of unsanitized data being passed to to file level functions.
     * <p>
     * This may be used for poison byte attacks.
     *
     * @param path the path to check
     */
    private static void failIfNullBytePresent(final String path) {
        final int len = path.length();
        for (int i = 0; i < len; i++) {
            if (path.charAt(i) == 0) {
                throw new IllegalArgumentException("Null byte present in file/path name. There are no " +
                        "known legitimate use cases for such data, but several injection attacks may use it");
            }
        }
    }


}

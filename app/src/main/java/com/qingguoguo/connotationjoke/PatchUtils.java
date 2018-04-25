package com.qingguoguo.connotationjoke;

/**
 * @author :qingguoguo
 * @datetime ：2018/4/25
 * @describe :
 */

public class PatchUtils {

    /**
     * @param oldApkPath 原来的Apk路径
     * @param newApkPath 合并后新的Apk路径
     * @param patchPath
     */
    public static native void combine(String oldApkPath, String newApkPath, String patchPath);

}

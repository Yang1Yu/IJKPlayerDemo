
package com.hejia.media;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author mengxiangru
 * @date 2016-10-29---����11:01:43
 * @parameter
 * @return
 */

public class FilePerate implements FileFilter {

	List<String> fileList = null;// 文件数组，用于存放当前目录的所有子目录
	static String currentPath = null;// 当前目录路径
	Context context = null;
	int fileNum = 0;// 文件数目
	int folderNum = 0;// 目录文件
	public static File file = null;
	public boolean IsDirectory = false;
	public static String filename;

	// path的堆栈
	private static Stack<String> pathStack;

	public FilePerate() {
		fileList = new ArrayList<String>();
		currentPath = getStorageRootFolder();// 初始化当前目录，为内置SD卡的根目录

	}

	// 得到根目录
	public String getStorageRootFolder() {

		if ((Environment.getExternalStorageState()).equals(Environment.MEDIA_MOUNTED)) {
			// 如果内置SD卡存在，则返回内置SD卡的目录

			return Environment.getExternalStorageDirectory().getAbsolutePath();// 得到内置SD卡的目录的路径

		} else {
			return null;
		}

	}

	// 判断是否有SD卡
	private boolean ExistSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	// 判断是否存在USB /mnt/media_rw/udisk
	private List<String> listUSBOrSD;
	private Iterator iteratorUSBOrSD;

	public boolean existUSB() {
		listUSBOrSD = getExtSDCardPathList();
		iteratorUSBOrSD = listUSBOrSD.iterator();
		while (iteratorUSBOrSD.hasNext()) {
			if ("/mnt/media_rw/udisk".equals(iteratorUSBOrSD.next())) {
				return true;
			}
		}
		return false;
	}

	// 判断是否存在外置SD卡 /mnt/media_rw/extsd
	public boolean existSD() {
		listUSBOrSD = getExtSDCardPathList();
		iteratorUSBOrSD = listUSBOrSD.iterator();
		while (iteratorUSBOrSD.hasNext()) {
			if ("/mnt/media_rw/extsd".equals(iteratorUSBOrSD.next())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件目录
	 *
	 * @return
	 */
	public File getDirectoryFile(String dirPath) {

		String storageState = Environment.getExternalStorageState();

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + dirPath;

			file = new File(filePath);

			if (!file.exists()) {
				// 建立一个新的目录
				file.mkdirs();
			}
		}

		return file;
	}

	// 选择的子目录
	public List<String> selectFolder(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();// 得到所有子文件和目录
		// 过滤隐藏文件
		FileFilter fileFilter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				// return !pathname.isHidden();
				filename = pathname.getPath();
				if (pathname.isDirectory())
					return true;
				// 过滤隐藏文件
				if (filename.endsWith(".mp3"))
					return true;
				if (filename.endsWith(".mp4"))
					return true;
				else
					return false;
			}
		};
		files = folder.listFiles(fileFilter);
		fileList.clear();// 清空文件列表
		setFileNum(0);// 重新设置文件和目录的个数
		setFolderNum(0);
		// 重新生成文件列表
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					fileNum++;// 统计当前文件夹下的文件个数

				} else {
					folderNum++;
				}
				fileList.add(file.getName());// 将文件加入列表
			}
		}
		setCurrentFolder(path);// 重新设置当前路径
		return fileList;
	}

	// 选择的为文件
	public void selectFile(String path) {

		System.out.println("选中了文件");
	}

	// 得到当前目录的子目录
	public List<String> getAllFile(String path) {
		File filePath = new File(path);
		if (filePath.isFile()) {
			// 如果点击的是文件,则对文件做相关的操作
			selectFile(path);
			return null;
		} else {
			selectFolder(path);
		}
		return fileList;
	}

	// 得到上级目录
	public String getParentFolder(String path) {
		File folder = new File(path);
		if ((!folder.equals(getStorageRootFolder())) || (!folder.equals(getCardRootFolder().get(1))) || (!folder.equals(getCardRootFolder().get(1)))) {// ������Ǹ�Ŀ¼�򷵻��ϼ�Ŀ¼��·��
			return folder.getParent();
		}
		return null;
	}

	/**
	 * 获取堆栈最上层的路径
	 *
	 * @return
	 */
	public String getLastPath() {
		return pathStack.lastElement();
	}

	/**
	 * 移除堆栈最上层路径
	 */
	public void removeLastPath() {
		pathStack.remove(getLastPath());
	}

	// 得到当前的路径
	public static String getCurrentPath() {
		return currentPath;
	}

	// 得到当前的路径
	public void setCurrentFolder(String path) {
		currentPath = path;
	}

	// 得到文件列表
	public List<String> getFileList() {
		return fileList;
	}

	// 得到文件个数
	public int getFileNum() {
		return fileNum;
	}

	// 判断是否为文件
	public boolean file(File file) {
		// file.isFile();
		return file.isFile();
	}


	// 设置文件个数
	public void setFileNum(int num) {
		this.fileNum = num;
	}

	public int getFolderNum() {
		return folderNum;
	}

	// 设置目录个数
	public void setFolderNum(int num) {
		this.folderNum = num;
	}

	// 得到文件和目录名的列表
	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		if (!pathname.getName().startsWith(".")) {
			return true;
		} else {
			return false;
		}
	}

	public List<String> getCardRootFolder() {
		List<String> paths = getExtSDCardPathList();
		if (paths.size() >= 2) {
			return paths;
		} else
			return null;
	}


	/**
	 * 获取所有内存路径
	 *
	 * @return 应该就一条记录或空
	 */
	public static List<String> getExtSDCardPathList() {
		List<String> paths = new ArrayList<String>();
		String extFileStatus = Environment.getExternalStorageState();
		File extFile = Environment.getExternalStorageDirectory();
		// 首先判断一下SD卡的状态，处于挂载状态才能获取的到
		if (extFileStatus.equals(Environment.MEDIA_MOUNTED) && extFile.exists() && extFile.isDirectory()
				&& extFile.canWrite()) {
			// SD卡的路径
			paths.add(extFile.getAbsolutePath());
		}
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("mount");
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			int mountPathIndex = 1;
			while ((line = br.readLine()) != null) {
				if ((!line.contains("fat") && !line.contains("fuse") && !line.contains("storage"))
						|| line.contains("secure") || line.contains("asec") || line.contains("firmware")
						|| line.contains("shell") || line.contains("obb") || line.contains("legacy")
						|| line.contains("data")) {
					continue;
				}
				String[] parts = line.split(" ");
				int length = parts.length;
				if (mountPathIndex >= length) {
					continue;
				}
				String mountPath = parts[mountPathIndex];
				if (!mountPath.contains("/") || mountPath.contains("data") || mountPath.contains("Data")) {
					continue;
				}
				File mountRoot = new File(mountPath);
				if (!mountRoot.exists() || !mountRoot.isDirectory() || !mountRoot.canWrite()) {
					continue;
				}
				boolean equalsToPrimarySD = mountPath.equals(extFile.getAbsolutePath());
				if (equalsToPrimarySD) {
					continue;
				}
				// 扩展存储卡即TF卡或者SD卡路径
				paths.add(mountPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paths;
	}

}

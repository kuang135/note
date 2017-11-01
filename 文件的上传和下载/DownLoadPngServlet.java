package download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DownLoadUtils;

public class DownLoadPngServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 开始coding................ 下载服务器对应资源 1.png 查找目标文件所在的服务器的路径位置
		String path = this.getServletContext().getRealPath(
				"/upload/adasdas asdas dasasda dasda.png");
		// 判断该文件是否存在
		File f = new File(path);
		if (f.exists()) {
			// 文件存在 执行下载 操作
			String filename = f.getName();
			// 1 Mime 附件
			response.setContentType(this.getServletContext().getMimeType(
					filename));

			/**
			 * 判断浏览器的类型 采用不同的编码输出
			 * 
			 * 如何判断浏览器是哪一种?
			 * 
			 */
			String browserType = request.getHeader("user-agent");// 获取浏览器的类型
			filename = DownLoadUtils.getAttachmentFileName(filename,
					browserType);

			// 2: 附件形式
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);

			// 按照不同的浏览器解码方式 进行代码控制输出
			// 服务器中文 编码输出 浏览器解码 即可 IE 默认按照 URL编码 火狐 BASE64

			// 3: 流 读取服务器的资源到浏览器客户端 流的拷贝

			InputStream in = new FileInputStream(f);
			int len = 0;
			byte[] bytes = new byte[4 * 1024];
			while ((len = in.read(bytes)) != -1) {
				response.getOutputStream().write(bytes, 0, len);
			}
			in.close();

		} else {

			throw new RuntimeException("该文件不存在!");
		}

	}



}

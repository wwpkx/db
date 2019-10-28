package cn.itcast.jdbc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * 2008-12-6
 * 
 * @author <a href="mailto:liyongibm@gmail.com">liyong</a>
 * 
 */
public class BlobTest {

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException, IOException {
		// create();
		read();
	}

	static void read() throws SQLException, IOException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 2.��������
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.�������
			st = conn.createStatement();

			// 4.ִ�����
			rs = st.executeQuery("select big_bit  from blob_test");

			// 5.������
			while (rs.next()) {
				// Blob blob = rs.getBlob(1);
				// InputStream in = blob.getBinaryStream();
				InputStream in = rs.getBinaryStream("big_bit");

				File file = new File("IMG_0002_bak.jpg");
				OutputStream out = new BufferedOutputStream(
						new FileOutputStream(file));
				byte[] buff = new byte[1024];
				for (int i = 0; (i = in.read(buff)) > 0;) {
					out.write(buff, 0, i);
				}
				out.close();
				in.close();
			}
		} finally {
			JdbcUtils.free(rs, st, conn);
		}
	}

	static void create() throws SQLException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 2.��������
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.�������
			String sql = "insert into blob_test(big_bit) values (?) ";
			ps = conn.prepareStatement(sql);
			File file = new File("IMG_0002.jpg");
			InputStream in = new BufferedInputStream(new FileInputStream(file));

			ps.setBinaryStream(1, in, (int) file.length());
			// 4.ִ�����
			int i = ps.executeUpdate();
			in.close();

			System.out.println("i=" + i);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}
}

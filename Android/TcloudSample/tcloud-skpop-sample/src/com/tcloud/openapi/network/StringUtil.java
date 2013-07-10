package com.tcloud.openapi.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {

	public static String inputStreamToString(final InputStream stream) {
		final BufferedReader br = new BufferedReader(new InputStreamReader(stream), 8192);
		// StringBuilder sb = new StringBuilder();
		final StringBuffer sb = new StringBuffer(10240);
		String line = null;
		final String linesep = System.getProperty("line.separator"); // "\n"
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(linesep);
			}
		}
		catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			br.close();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}

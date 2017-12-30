package com.mattczyr.cursecompare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.swing.UIManager;

public class CurseCompare {

	public static boolean working = false;

	public static void main(String[] args) {
		if (args.length == 2) {
			compare(args[0], args[1]);
		} else {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Interface.openInterface();
		}
	}

	public static void compare(String url0, String url1) {
		if (working) {
			return;
		}

		long startTime = System.nanoTime();

		working = true;
		String[] urls = new String[2];
		urls[0] = url0;
		urls[1] = url1;
		String[] names = new String[2];
		setStatus("Identifying...");
		try {
			for (int i = 0; i < 2; i++) {
				if (!urls[i].startsWith("https://")) {
					urls[i] = "https://" + urls[i];
				}

				if (!urls[i].contains("minecraft.curseforge.com") && !urls[i].contains("feed-the-beast.com")) {
					error();
					return;
				}

				String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36";
				URL url = new URL(urls[i]);
				URLConnection connection = url.openConnection();
				connection.setRequestProperty("User-Agent", userAgent);
				InputStream stream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String line = reader.readLine();
				while (line != null) {
					if (line.contains("<title>")) {
						names[i] = line.substring(line.indexOf("Overview - ") + 11, line.indexOf(" - Modpacks"));
						break;
					}
					line = reader.readLine();
				}
			}
		} catch (IOException e) {
			error();
			return;
		}

		Interface.setNames(names[0], names[1]);

		setStatus("Gathering dependencies...");
		Map<String, Integer> mods = new TreeMap<String, Integer>();
		for (int i = 0; i < 2; i++) {
			String path = urls[i] + "/relations/dependencies";
			int page = 1;
			boolean done = false;
			while (!done) {
				try {
					String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36";
					URL url = new URL(path + "?page=" + page);
					URLConnection connection = url.openConnection();
					connection.setRequestProperty("User-Agent", userAgent);
					InputStream stream = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					String line = reader.readLine();
					boolean upcoming = false;
					while (line != null) {
						if (upcoming) {
							String name = line.substring(line.indexOf(">") + 1, line.length() - 4);
							if (mods.get(name) == null) {
								mods.put(name, i + 1);
							} else if (mods.get(name) == i + 1 || mods.get(name) == 3) {
								done = true;
								break;
							} else {
								mods.put(name, mods.get(name) + i + 1);
							}
							upcoming = false;
						} else if (line.contains("name-wrapper overflow-tip")) {
							upcoming = true;
						}
						line = reader.readLine();
					}
					page++;
				} catch (IOException e) {
					error();
					return;
				}
			}
		}

		Interface.stop(false);
		setStatus("Comparing...");

		int num = 0;
		consoleLog("\nMods included by both " + names[0] + " and " + names[1] + ":");
		for (Map.Entry<String, Integer> entry : mods.entrySet()) {
			if (entry.getValue() == 3) {
				output(convertString(entry.getKey()), 0);
				num++;
			}
		}
		Interface.setNum(num, 0);

		num = 0;
		consoleLog("\nMods included only by " + names[0] + ":");
		for (Map.Entry<String, Integer> entry : mods.entrySet()) {
			if (entry.getValue() == 1) {
				output(convertString(entry.getKey()), 1);
				num++;
			}
		}
		Interface.setNum(num, 1);

		num = 0;
		consoleLog("\nMods included only by " + names[1] + ":");
		for (Map.Entry<String, Integer> entry : mods.entrySet()) {
			if (entry.getValue() == 2) {
				output(convertString(entry.getKey()), 2);
				num++;
			}
		}
		Interface.setNum(num, 2);
		consoleLog("\n");

		float elapsedTime = (float) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) / 1000;
		setStatus("Completed in " + elapsedTime + "s");
		consoleLog("\n");
	}

	private static String convertString(String s) {
		StringBuilder sb = new StringBuilder(s);
		while (sb.toString().contains("&#x")) {
			int start = s.indexOf("&#x");
			int end = s.indexOf(";");
			String sub = s.substring(start + 3, end);
			String converted = convertHexToString(sub);
			sb.delete(start, end + 1);
			sb.insert(start, converted);
		}

		while (sb.toString().contains("&amp;")) {
			int start = s.indexOf("&amp;");
			sb.delete(start, start + 5);
			sb.insert(start, "&");
		}

		return sb.toString();
	}

	private static String convertHexToString(String hex) {
		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
			temp.append(decimal);
		}

		return sb.toString();
	}

	private static void output(String s, int index) {
		Interface.output(s, index);
		consoleLog(s);
	}

	private static void setStatus(String s) {
		Interface.setStatus(s);
		consoleLog(s);
	}

	private static void consoleLog(String s) {
		System.out.println(s);
	}

	private static void error() {
		setStatus("Invalid URL. Please provide only minecraft.curseforge.com or feed-the-beast.com project URLs.");
		Interface.error();
	}

}

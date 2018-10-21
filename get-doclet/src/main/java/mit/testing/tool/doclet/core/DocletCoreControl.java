package mit.testing.tool.doclet.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

public class DocletCoreControl extends Doclet {

	// entry point
	public static boolean start(RootDoc rootDoc) {
		File file = new File("doclet-methodlist.csv");

		try {
			PrintWriter out = new PrintWriter(file);
			try {
				writeTo(out, rootDoc);

				if (out.checkError()) {
					return false;
				}
			} finally {
				out.close();
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return true;
	}

	public static LanguageVersion languageVersion() {
		return LanguageVersion.JAVA_1_5;
	}

	public static void writeTo(PrintWriter out, RootDoc rootDoc) {
		// header
		out.println(join(new String[] { "パッケージ", "完全修飾クラス名", "クラス名（英語）", "クラス名（日本語）", "修飾子", "メソッド名（英語）", "メソッド名（日本語）",
				"メソッド概要", "引数内容", "戻り値内容" }));

		// body
		for (ClassDoc classDoc : rootDoc.classes()) {
			for (MethodDoc methodDoc : classDoc.methods(true)) {
				String[] a = new String[11];

				Arrays.fill(a, "");
				int i = -1;

				a[++i] = classDoc.containingPackage().name();
				a[++i] = classDoc.qualifiedName();
				a[++i] = classDoc.name();
				a[++i] = getFirstLine(classDoc.commentText());
				a[++i] = methodDoc.modifiers();
				a[++i] = methodDoc.name();
				a[++i] = getFirstLine(methodDoc.commentText());
				a[++i] = getSecondLine(methodDoc.commentText());
				a[++i] = getParam(methodDoc);
				a[++i] = printReturnTag(methodDoc);

				out.println(join(a));
			}
		}
	}

	private static String getFirstLine(String doc) {
		if (doc == null || doc == "") {
			return "";
		}

		String[] parts = doc.split("[\r\n]");

		if (parts[0] == null || parts[0] == "") {
			return "";
		}
		return parts[0].replaceAll("<.*>", "").replaceAll("\"", "\"\"").replaceAll(",", "、");
	}

	private static String getSecondLine(String doc) {

		StringBuilder s = new StringBuilder();

		if (doc == null || doc == "") {
			return "";
		}

		String[] parts = doc.split("[\r\n]");

		if (parts.length < 2) {
			return "";
		} else if (parts[1] == null || parts[1] == "") {
			return "";
		}

		for (int h = 1; h < parts.length; h++) {
			String part = "";

			part = parts[h].replaceAll("<.*>", "").replaceAll("\"", "\"\"").replaceAll(",", "、");
			part = part.trim();

			if (h == 1) {
				s.append(part);
			} else {
				s.append("、");
				s.append(part);
			}
		}
		return s.toString();
	}

	private static String getParam(MethodDoc m) {
		StringBuilder s = new StringBuilder();

		if (m == null || m.paramTags() == null) {
			return "";
		}

		ParamTag[] params = m.paramTags();

		if (params.length < 1) {
			return "";
		}

		for (int i = 0; i < params.length; i++) {
			String paramComment = "";

			paramComment = params[i].parameterComment().replaceAll("[\r\n]", "").replaceAll("<.*>", "")
					.replaceAll("\"", "\"\"").replaceAll(",", "、");
			paramComment = paramComment.trim();

			if (i == 0) {
				s.append(params[i].parameterName() + " - " + paramComment);
			} else {
				s.append("、").append(params[i].parameterName() + " - " + paramComment);
			}
		}
		return s.toString();
	}

	private static String printReturnTag(MethodDoc methodDoc) {
		StringBuilder s = new StringBuilder();

		if (methodDoc == null) {
			return "";
		}

		if (methodDoc.tags("return") == null) {
			return "";
		}

		Tag[] returns = methodDoc.tags("return");

		for (int j = 0; j < returns.length; j++) {
			String returnComment = "";
			returnComment = returns[j].text().replaceAll("[\r\n]", "").replaceAll("<.*>", "").replaceAll("\"", "\"\"")
					.replaceAll(",", "、");
			returnComment = returnComment.trim();

			if (j == 0) {
				s.append(returnComment);
			} else {
				s.append("、").append(returnComment);
			}
		}
		return s.toString();
	}

	private static <T> String join(T[] a) {
		if (a.length == 0) {
			return "";
		}
		StringBuilder s = new StringBuilder(String.valueOf(a[0]));

		for (int i = 1; i < a.length; i++) {
			s.append(",").append(a[i]);
		}

		return s.toString();
	}
}

package org.springframework.security.data.acl.spel.util;

public abstract class ReflectionUtils {

	public static boolean declareInterface(Class<?> source, Class<?> search) {
		boolean isExist = false;
		String className = search.getName();
		Class<?>[] in = source.getInterfaces();
		if (in != null && in.length > 0) {
			for (Class<?> clzz : in) {
				if (className.equals(clzz.getName())) {
					isExist = true;
					break;
				}
			}
		}
		return isExist;
	}
}

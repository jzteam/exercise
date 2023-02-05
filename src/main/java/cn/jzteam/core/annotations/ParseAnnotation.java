package cn.jzteam.core.annotations;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ParseAnnotation {

	public static void parseFieldAnnotation(Object obj) {
		Class<? extends Object> clazz = obj.getClass();

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Annotation[] annotations = field.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				if(annotation instanceof Evaluate){
					System.out.println(annotation.getClass().getName()+" �� Evaluate������");
				}
				//String simpleName = annotation.getClass().getSuperclass().getSimpleName();
				String simpleName = "Evaluate";
				String parseMethodName = "parse" + simpleName;
				System.out.println(field.getName());
				try {
					Method parseMethod = ParseAnnotation.class.getDeclaredMethod(parseMethodName,Object.class, Field.class,
							Annotation.class);
					parseMethod.setAccessible(true);
					try {
						parseMethod.invoke(null, obj, field, annotation);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void parseEvaluate(Object obj, Field field, Annotation annotation) {
		Evaluate evaluate = (Evaluate)annotation;
		boolean required = evaluate.required();
		if(required){
			String fieldObjectName = field.getType().getName();
			try {
				Object fieldObject = Class.forName(fieldObjectName).newInstance();
				field.setAccessible(true);
				field.set(obj, fieldObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

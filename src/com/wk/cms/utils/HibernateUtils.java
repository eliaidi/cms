package com.wk.cms.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.wk.cms.model.annotations.Show;
import com.wk.cms.model.annotations.ShowArea;

public class HibernateUtils {

	public static Projection getProjections(Class<?> class1, ShowArea list) {

		List<String> showFields = getShowFields(class1, list);
		if (CommonUtils.isEmpty(showFields)) {
			return null;
		}

		ProjectionList pl = Projections.projectionList();
		for (String fields : showFields) {
			pl.add(Projections.property(fields).as(fields));
		}
		return pl;
	}

	public static List<String> getShowFields(Class<?> class1, ShowArea list) {

		List<String> fs = new ArrayList<String>();
		Field[] fields = class1.getDeclaredFields();

		for (Field field : fields) {
			if (field.getAnnotation(OneToMany.class) != null&&field.getAnnotation(OneToMany.class).fetch()!=FetchType.EAGER) {
				continue;
			}
			Show show = field.getAnnotation(Show.class);
			if (show == null || show.value().equals(ShowArea.ALL)
					|| show.value().equals(list)) {
				fs.add(field.getName());
			}
		}
		return fs;
	}

}

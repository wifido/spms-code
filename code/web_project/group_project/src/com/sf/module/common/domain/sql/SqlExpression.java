package com.sf.module.common.domain.sql;

import com.google.common.base.Joiner;
import java.util.List;
import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.domain.sql.SqlHelper.toSqlValue;

public class SqlExpression extends SqlSnippet {
	public static final String WHERE = " where ";
	public static final String AND = " and ";

	public static final String LIKE = " LIKE ";
	public static final String QUERY_ARG = "?";
	public static final String COLON = " :";

	protected SqlExpression(CharSequence content) {
		super(content);
	}

	protected SqlExpression(String format, Object... args) {
		super(format, args);
	}

	public SqlExpression and(CharSequence another) {
		builder.append(" AND (").append(another).append(")");

		return this;
	}

	public SqlExpression or(CharSequence another) {
		builder.append(" OR (").append(another).append(")");

		return this;
	}

	public static SqlExpression in(CharSequence columnName, int argsCount) {
		List<String> args = newArrayList();
		for (int index = 0; index < argsCount; index++) {
			args.add(QUERY_ARG);
		}

		String join = Joiner.on(",").join(args);

		return withinChild(columnName, join);
	}

	public static SqlExpression withinChild(CharSequence leftHand, CharSequence childQueryStatement) {
		return new SqlExpression(leftHand + " IN ( " + childQueryStatement + " ) ");
	}

	public static SqlExpression expression(CharSequence expression) {
		return new SqlExpression("(%s)", expression);
	}

	public static SqlExpression expression(CharSequence leftHand, String operator, CharSequence rightHand) {
		return new SqlExpression("%s %s %s", leftHand, operator, rightHand);
	}

	public static SqlExpression expression(CharSequence leftHand, String operator, Object rightHand) {
		return expression(leftHand, operator, toSqlValue(rightHand));
	}

	public static SqlExpression isNull(CharSequence leftHand) {
		return expression(leftHand, "IS", "NULL");
	}

	public static SqlExpression isNotNull(CharSequence leftHand) {
		return expression(leftHand, "IS NOT ", "NULL");
	}

	public static SqlExpression outjoin(CharSequence leftHand, CharSequence rightHand) {
		return expression(leftHand, "=", rightHand + "(+)");
	}
	public static SqlExpression equal(CharSequence leftHand, CharSequence rightHand) {
		return expression(leftHand, "=", rightHand);
	}

	public static SqlExpression equal(CharSequence leftHand, Object rightHand) {
		return equal(leftHand, toSqlValue(rightHand));
	}

	public static SqlExpression notEqual(CharSequence leftHand, CharSequence rightHand) {
		return expression(leftHand, "<>", rightHand);
	}

	public static SqlExpression notEqual(CharSequence leftHand, Object rightHand) {
		return notEqual(leftHand, toSqlValue(rightHand));
	}

	public static SqlExpression like(CharSequence leftHand, CharSequence rightHand) {
		return expression(leftHand, LIKE, rightHand);
	}
}

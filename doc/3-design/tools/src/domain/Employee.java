package domain;

public class Employee {
    private final String userCode;
    private final String username;
    private final String userSex;
    private final String workType;
    private final String dutyName;
    private final String departmentCode;
    private final String postType;

    public Employee(String userCode, String username, String userSex,
                    String workType, String dutyName, String departmentCode,
                    String postType) {
        this.userCode = userCode;
        this.username = username;
        this.userSex = userSex;
        this.workType = workType;
        this.dutyName = dutyName;
        this.departmentCode = departmentCode;
        this.postType = postType;
    }

    private String getPaddedUserCode() {
        double userCodeWithDouble = Double.parseDouble(userCode);
        int userCodeWithInteger = (int) userCodeWithDouble;
        return leftPadding("0", String.valueOf(userCodeWithInteger), 6);
    }

    private static String leftPadding(String targetReplaceChar,
                                      String sourceChar, int maxLength) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < maxLength - sourceChar.length(); index++) {
            stringBuilder.append(targetReplaceChar);
        }

        stringBuilder.append(sourceChar);
        return stringBuilder.toString();
    }

    private String splittedDepartmentCode() {
        int indexOfDot = departmentCode.indexOf(".");

        return departmentCode.substring(0, indexOfDot);
    }

    private int getWorkType() {
        for (WORKTYPE workType : WORKTYPE.values()) {
            if (workType.name.equals(this.workType)) {
                return workType.ordinal();
            }
        }

        return WORKTYPE.UNKNOWN.ordinal();
    }

    public boolean isOperator() {
        return "仓管员".equals(dutyName) || "仓管组长".equals(dutyName);
    }

    public boolean isOperation() {
        return "包裹处理序列".equals(postType);
    }

    private String getPostType() {
        if (isOperator()) {
            return "3";
        }
        if (isOperation()) {
            return "1";
        }
        return "";
    }

    public String toSql() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" insert into tm_oss_employee");
        stringBuilder.append("(");

        stringBuilder
                .append(" EMP_ID, EMP_CODE, EMP_NAME, EMP_DUTY_NAME, DEPT_ID, WORK_TYPE, EMP_POST_TYPE");

        stringBuilder.append(")");
        stringBuilder.append("values(");

        stringBuilder.append("seq_px_base.nextval,");
        stringBuilder.append("'" + getPaddedUserCode() + "'");
        stringBuilder.append(",");
        stringBuilder.append("'" + username + "',");
        stringBuilder.append("'" + dutyName + "',");
        stringBuilder
                .append("(select dept.dept_id from tm_department dept where dept.dept_code='"
                        + splittedDepartmentCode() + "')" + ",");

        stringBuilder.append(getWorkType() + ",");
        stringBuilder.append(getPostType());

        stringBuilder.append(");");
        if (this.getPaddedUserCode().length() != 6) {
            System.out.println("error");
        }
//		System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public enum WORKTYPE {
        UNKNOWN("非全日制工"),
        HOURLY_WORKER("非全日制工"),
        BASE_TRAINEE("基地见习生"),
        LABOR_DISPATCH("劳务派遣"),
        FULL_TIME("全日制员工"),
        TRAINEE("实习生");

        public final String name;

        private WORKTYPE(String name) {
            this.name = name;
        }
    }
}
package com.sf.module.common.domain;

public enum EmployeeType {
    OPERATION_PERSONNEL {
        @Override
        public String getEmployeeType() {
            return "1";
        }
    },DISPATCH_PERSONNEL {
        @Override
        public String getEmployeeType() {
            return "2";
        }
    },WAREHOUSE_PERSONNEL {
        @Override
        public String getEmployeeType() {
            return "3";
        }
    },CUSTOMER_SERVICE {
        @Override
        public String getEmployeeType() {
            return "4";
        }
    },DRIVER_PERSONNEL {
        @Override
        public String getEmployeeType() {
            return "5";
        }
    };

    public abstract String getEmployeeType();
}

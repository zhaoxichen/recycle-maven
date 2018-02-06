package com.lianjiu.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdersFurnitureExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrdersFurnitureExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andOrFurnitureIdIsNull() {
            addCriterion("or_furniture_id is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdIsNotNull() {
            addCriterion("or_furniture_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdEqualTo(String value) {
            addCriterion("or_furniture_id =", value, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdNotEqualTo(String value) {
            addCriterion("or_furniture_id <>", value, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdGreaterThan(String value) {
            addCriterion("or_furniture_id >", value, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdGreaterThanOrEqualTo(String value) {
            addCriterion("or_furniture_id >=", value, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdLessThan(String value) {
            addCriterion("or_furniture_id <", value, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdLessThanOrEqualTo(String value) {
            addCriterion("or_furniture_id <=", value, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdLike(String value) {
            addCriterion("or_furniture_id like", value, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdNotLike(String value) {
            addCriterion("or_furniture_id not like", value, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdIn(List<String> values) {
            addCriterion("or_furniture_id in", values, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdNotIn(List<String> values) {
            addCriterion("or_furniture_id not in", values, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdBetween(String value1, String value2) {
            addCriterion("or_furniture_id between", value1, value2, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureIdNotBetween(String value1, String value2) {
            addCriterion("or_furniture_id not between", value1, value2, "orFurnitureId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIsNull() {
            addCriterion("category_id is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIsNotNull() {
            addCriterion("category_id is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryIdEqualTo(Long value) {
            addCriterion("category_id =", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotEqualTo(Long value) {
            addCriterion("category_id <>", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdGreaterThan(Long value) {
            addCriterion("category_id >", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdGreaterThanOrEqualTo(Long value) {
            addCriterion("category_id >=", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLessThan(Long value) {
            addCriterion("category_id <", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLessThanOrEqualTo(Long value) {
            addCriterion("category_id <=", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIn(List<Long> values) {
            addCriterion("category_id in", values, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotIn(List<Long> values) {
            addCriterion("category_id not in", values, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdBetween(Long value1, Long value2) {
            addCriterion("category_id between", value1, value2, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotBetween(Long value1, Long value2) {
            addCriterion("category_id not between", value1, value2, "categoryId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdIsNull() {
            addCriterion("or_furniture_alliance_id is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdIsNotNull() {
            addCriterion("or_furniture_alliance_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdEqualTo(String value) {
            addCriterion("or_furniture_alliance_id =", value, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdNotEqualTo(String value) {
            addCriterion("or_furniture_alliance_id <>", value, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdGreaterThan(String value) {
            addCriterion("or_furniture_alliance_id >", value, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdGreaterThanOrEqualTo(String value) {
            addCriterion("or_furniture_alliance_id >=", value, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdLessThan(String value) {
            addCriterion("or_furniture_alliance_id <", value, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdLessThanOrEqualTo(String value) {
            addCriterion("or_furniture_alliance_id <=", value, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdLike(String value) {
            addCriterion("or_furniture_alliance_id like", value, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdNotLike(String value) {
            addCriterion("or_furniture_alliance_id not like", value, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdIn(List<String> values) {
            addCriterion("or_furniture_alliance_id in", values, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdNotIn(List<String> values) {
            addCriterion("or_furniture_alliance_id not in", values, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdBetween(String value1, String value2) {
            addCriterion("or_furniture_alliance_id between", value1, value2, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureAllianceIdNotBetween(String value1, String value2) {
            addCriterion("or_furniture_alliance_id not between", value1, value2, "orFurnitureAllianceId");
            return (Criteria) this;
        }

        public Criteria andAddressIdIsNull() {
            addCriterion("address_id is null");
            return (Criteria) this;
        }

        public Criteria andAddressIdIsNotNull() {
            addCriterion("address_id is not null");
            return (Criteria) this;
        }

        public Criteria andAddressIdEqualTo(String value) {
            addCriterion("address_id =", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotEqualTo(String value) {
            addCriterion("address_id <>", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdGreaterThan(String value) {
            addCriterion("address_id >", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdGreaterThanOrEqualTo(String value) {
            addCriterion("address_id >=", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLessThan(String value) {
            addCriterion("address_id <", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLessThanOrEqualTo(String value) {
            addCriterion("address_id <=", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLike(String value) {
            addCriterion("address_id like", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotLike(String value) {
            addCriterion("address_id not like", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdIn(List<String> values) {
            addCriterion("address_id in", values, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotIn(List<String> values) {
            addCriterion("address_id not in", values, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdBetween(String value1, String value2) {
            addCriterion("address_id between", value1, value2, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotBetween(String value1, String value2) {
            addCriterion("address_id not between", value1, value2, "addressId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusIsNull() {
            addCriterion("or_furniture_status is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusIsNotNull() {
            addCriterion("or_furniture_status is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusEqualTo(Byte value) {
            addCriterion("or_furniture_status =", value, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusNotEqualTo(Byte value) {
            addCriterion("or_furniture_status <>", value, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusGreaterThan(Byte value) {
            addCriterion("or_furniture_status >", value, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("or_furniture_status >=", value, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusLessThan(Byte value) {
            addCriterion("or_furniture_status <", value, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusLessThanOrEqualTo(Byte value) {
            addCriterion("or_furniture_status <=", value, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusIn(List<Byte> values) {
            addCriterion("or_furniture_status in", values, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusNotIn(List<Byte> values) {
            addCriterion("or_furniture_status not in", values, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusBetween(Byte value1, Byte value2) {
            addCriterion("or_furniture_status between", value1, value2, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("or_furniture_status not between", value1, value2, "orFurnitureStatus");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdIsNull() {
            addCriterion("or_furniture_brothers_id is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdIsNotNull() {
            addCriterion("or_furniture_brothers_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdEqualTo(String value) {
            addCriterion("or_furniture_brothers_id =", value, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdNotEqualTo(String value) {
            addCriterion("or_furniture_brothers_id <>", value, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdGreaterThan(String value) {
            addCriterion("or_furniture_brothers_id >", value, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdGreaterThanOrEqualTo(String value) {
            addCriterion("or_furniture_brothers_id >=", value, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdLessThan(String value) {
            addCriterion("or_furniture_brothers_id <", value, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdLessThanOrEqualTo(String value) {
            addCriterion("or_furniture_brothers_id <=", value, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdLike(String value) {
            addCriterion("or_furniture_brothers_id like", value, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdNotLike(String value) {
            addCriterion("or_furniture_brothers_id not like", value, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdIn(List<String> values) {
            addCriterion("or_furniture_brothers_id in", values, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdNotIn(List<String> values) {
            addCriterion("or_furniture_brothers_id not in", values, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdBetween(String value1, String value2) {
            addCriterion("or_furniture_brothers_id between", value1, value2, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureBrothersIdNotBetween(String value1, String value2) {
            addCriterion("or_furniture_brothers_id not between", value1, value2, "orFurnitureBrothersId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeIsNull() {
            addCriterion("or_furniture_visitTime is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeIsNotNull() {
            addCriterion("or_furniture_visitTime is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeEqualTo(Date value) {
            addCriterion("or_furniture_visitTime =", value, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeNotEqualTo(Date value) {
            addCriterion("or_furniture_visitTime <>", value, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeGreaterThan(Date value) {
            addCriterion("or_furniture_visitTime >", value, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeGreaterThanOrEqualTo(Date value) {
            addCriterion("or_furniture_visitTime >=", value, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeLessThan(Date value) {
            addCriterion("or_furniture_visitTime <", value, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeLessThanOrEqualTo(Date value) {
            addCriterion("or_furniture_visitTime <=", value, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeIn(List<Date> values) {
            addCriterion("or_furniture_visitTime in", values, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeNotIn(List<Date> values) {
            addCriterion("or_furniture_visitTime not in", values, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeBetween(Date value1, Date value2) {
            addCriterion("or_furniture_visitTime between", value1, value2, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureVisittimeNotBetween(Date value1, Date value2) {
            addCriterion("or_furniture_visitTime not between", value1, value2, "orFurnitureVisittime");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceIsNull() {
            addCriterion("orders_price is null");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceIsNotNull() {
            addCriterion("orders_price is not null");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceEqualTo(String value) {
            addCriterion("orders_price =", value, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceNotEqualTo(String value) {
            addCriterion("orders_price <>", value, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceGreaterThan(String value) {
            addCriterion("orders_price >", value, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceGreaterThanOrEqualTo(String value) {
            addCriterion("orders_price >=", value, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceLessThan(String value) {
            addCriterion("orders_price <", value, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceLessThanOrEqualTo(String value) {
            addCriterion("orders_price <=", value, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceLike(String value) {
            addCriterion("orders_price like", value, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceNotLike(String value) {
            addCriterion("orders_price not like", value, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceIn(List<String> values) {
            addCriterion("orders_price in", values, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceNotIn(List<String> values) {
            addCriterion("orders_price not in", values, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceBetween(String value1, String value2) {
            addCriterion("orders_price between", value1, value2, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersPriceNotBetween(String value1, String value2) {
            addCriterion("orders_price not between", value1, value2, "ordersPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceIsNull() {
            addCriterion("orders_retr_price is null");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceIsNotNull() {
            addCriterion("orders_retr_price is not null");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceEqualTo(String value) {
            addCriterion("orders_retr_price =", value, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceNotEqualTo(String value) {
            addCriterion("orders_retr_price <>", value, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceGreaterThan(String value) {
            addCriterion("orders_retr_price >", value, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceGreaterThanOrEqualTo(String value) {
            addCriterion("orders_retr_price >=", value, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceLessThan(String value) {
            addCriterion("orders_retr_price <", value, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceLessThanOrEqualTo(String value) {
            addCriterion("orders_retr_price <=", value, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceLike(String value) {
            addCriterion("orders_retr_price like", value, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceNotLike(String value) {
            addCriterion("orders_retr_price not like", value, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceIn(List<String> values) {
            addCriterion("orders_retr_price in", values, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceNotIn(List<String> values) {
            addCriterion("orders_retr_price not in", values, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceBetween(String value1, String value2) {
            addCriterion("orders_retr_price between", value1, value2, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrdersRetrPriceNotBetween(String value1, String value2) {
            addCriterion("orders_retr_price not between", value1, value2, "ordersRetrPrice");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeIsNull() {
            addCriterion("or_furniture_payTime is null");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeIsNotNull() {
            addCriterion("or_furniture_payTime is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeEqualTo(Date value) {
            addCriterion("or_furniture_payTime =", value, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeNotEqualTo(Date value) {
            addCriterion("or_furniture_payTime <>", value, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeGreaterThan(Date value) {
            addCriterion("or_furniture_payTime >", value, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeGreaterThanOrEqualTo(Date value) {
            addCriterion("or_furniture_payTime >=", value, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeLessThan(Date value) {
            addCriterion("or_furniture_payTime <", value, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeLessThanOrEqualTo(Date value) {
            addCriterion("or_furniture_payTime <=", value, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeIn(List<Date> values) {
            addCriterion("or_furniture_payTime in", values, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeNotIn(List<Date> values) {
            addCriterion("or_furniture_payTime not in", values, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeBetween(Date value1, Date value2) {
            addCriterion("or_furniture_payTime between", value1, value2, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurniturePaytimeNotBetween(Date value1, Date value2) {
            addCriterion("or_furniture_payTime not between", value1, value2, "orFurniturePaytime");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdIsNull() {
            addCriterion("or_furniture_handler_id is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdIsNotNull() {
            addCriterion("or_furniture_handler_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdEqualTo(String value) {
            addCriterion("or_furniture_handler_id =", value, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdNotEqualTo(String value) {
            addCriterion("or_furniture_handler_id <>", value, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdGreaterThan(String value) {
            addCriterion("or_furniture_handler_id >", value, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdGreaterThanOrEqualTo(String value) {
            addCriterion("or_furniture_handler_id >=", value, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdLessThan(String value) {
            addCriterion("or_furniture_handler_id <", value, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdLessThanOrEqualTo(String value) {
            addCriterion("or_furniture_handler_id <=", value, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdLike(String value) {
            addCriterion("or_furniture_handler_id like", value, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdNotLike(String value) {
            addCriterion("or_furniture_handler_id not like", value, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdIn(List<String> values) {
            addCriterion("or_furniture_handler_id in", values, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdNotIn(List<String> values) {
            addCriterion("or_furniture_handler_id not in", values, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdBetween(String value1, String value2) {
            addCriterion("or_furniture_handler_id between", value1, value2, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerIdNotBetween(String value1, String value2) {
            addCriterion("or_furniture_handler_id not between", value1, value2, "orFurnitureHandlerId");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelIsNull() {
            addCriterion("or_furniture_handler_tel is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelIsNotNull() {
            addCriterion("or_furniture_handler_tel is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelEqualTo(String value) {
            addCriterion("or_furniture_handler_tel =", value, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelNotEqualTo(String value) {
            addCriterion("or_furniture_handler_tel <>", value, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelGreaterThan(String value) {
            addCriterion("or_furniture_handler_tel >", value, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelGreaterThanOrEqualTo(String value) {
            addCriterion("or_furniture_handler_tel >=", value, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelLessThan(String value) {
            addCriterion("or_furniture_handler_tel <", value, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelLessThanOrEqualTo(String value) {
            addCriterion("or_furniture_handler_tel <=", value, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelLike(String value) {
            addCriterion("or_furniture_handler_tel like", value, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelNotLike(String value) {
            addCriterion("or_furniture_handler_tel not like", value, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelIn(List<String> values) {
            addCriterion("or_furniture_handler_tel in", values, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelNotIn(List<String> values) {
            addCriterion("or_furniture_handler_tel not in", values, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelBetween(String value1, String value2) {
            addCriterion("or_furniture_handler_tel between", value1, value2, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureHandlerTelNotBetween(String value1, String value2) {
            addCriterion("or_furniture_handler_tel not between", value1, value2, "orFurnitureHandlerTel");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewIsNull() {
            addCriterion("or_items_name_preview is null");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewIsNotNull() {
            addCriterion("or_items_name_preview is not null");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewEqualTo(String value) {
            addCriterion("or_items_name_preview =", value, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewNotEqualTo(String value) {
            addCriterion("or_items_name_preview <>", value, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewGreaterThan(String value) {
            addCriterion("or_items_name_preview >", value, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewGreaterThanOrEqualTo(String value) {
            addCriterion("or_items_name_preview >=", value, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewLessThan(String value) {
            addCriterion("or_items_name_preview <", value, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewLessThanOrEqualTo(String value) {
            addCriterion("or_items_name_preview <=", value, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewLike(String value) {
            addCriterion("or_items_name_preview like", value, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewNotLike(String value) {
            addCriterion("or_items_name_preview not like", value, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewIn(List<String> values) {
            addCriterion("or_items_name_preview in", values, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewNotIn(List<String> values) {
            addCriterion("or_items_name_preview not in", values, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewBetween(String value1, String value2) {
            addCriterion("or_items_name_preview between", value1, value2, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsNamePreviewNotBetween(String value1, String value2) {
            addCriterion("or_items_name_preview not between", value1, value2, "orItemsNamePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewIsNull() {
            addCriterion("or_items_pictrue_preview is null");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewIsNotNull() {
            addCriterion("or_items_pictrue_preview is not null");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewEqualTo(String value) {
            addCriterion("or_items_pictrue_preview =", value, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewNotEqualTo(String value) {
            addCriterion("or_items_pictrue_preview <>", value, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewGreaterThan(String value) {
            addCriterion("or_items_pictrue_preview >", value, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewGreaterThanOrEqualTo(String value) {
            addCriterion("or_items_pictrue_preview >=", value, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewLessThan(String value) {
            addCriterion("or_items_pictrue_preview <", value, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewLessThanOrEqualTo(String value) {
            addCriterion("or_items_pictrue_preview <=", value, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewLike(String value) {
            addCriterion("or_items_pictrue_preview like", value, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewNotLike(String value) {
            addCriterion("or_items_pictrue_preview not like", value, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewIn(List<String> values) {
            addCriterion("or_items_pictrue_preview in", values, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewNotIn(List<String> values) {
            addCriterion("or_items_pictrue_preview not in", values, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewBetween(String value1, String value2) {
            addCriterion("or_items_pictrue_preview between", value1, value2, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrItemsPictruePreviewNotBetween(String value1, String value2) {
            addCriterion("or_items_pictrue_preview not between", value1, value2, "orItemsPictruePreview");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedIsNull() {
            addCriterion("or_furniture_created is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedIsNotNull() {
            addCriterion("or_furniture_created is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedEqualTo(Date value) {
            addCriterion("or_furniture_created =", value, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedNotEqualTo(Date value) {
            addCriterion("or_furniture_created <>", value, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedGreaterThan(Date value) {
            addCriterion("or_furniture_created >", value, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("or_furniture_created >=", value, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedLessThan(Date value) {
            addCriterion("or_furniture_created <", value, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedLessThanOrEqualTo(Date value) {
            addCriterion("or_furniture_created <=", value, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedIn(List<Date> values) {
            addCriterion("or_furniture_created in", values, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedNotIn(List<Date> values) {
            addCriterion("or_furniture_created not in", values, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedBetween(Date value1, Date value2) {
            addCriterion("or_furniture_created between", value1, value2, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureCreatedNotBetween(Date value1, Date value2) {
            addCriterion("or_furniture_created not between", value1, value2, "orFurnitureCreated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedIsNull() {
            addCriterion("or_furniture_updated is null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedIsNotNull() {
            addCriterion("or_furniture_updated is not null");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedEqualTo(Date value) {
            addCriterion("or_furniture_updated =", value, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedNotEqualTo(Date value) {
            addCriterion("or_furniture_updated <>", value, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedGreaterThan(Date value) {
            addCriterion("or_furniture_updated >", value, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedGreaterThanOrEqualTo(Date value) {
            addCriterion("or_furniture_updated >=", value, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedLessThan(Date value) {
            addCriterion("or_furniture_updated <", value, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedLessThanOrEqualTo(Date value) {
            addCriterion("or_furniture_updated <=", value, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedIn(List<Date> values) {
            addCriterion("or_furniture_updated in", values, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedNotIn(List<Date> values) {
            addCriterion("or_furniture_updated not in", values, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedBetween(Date value1, Date value2) {
            addCriterion("or_furniture_updated between", value1, value2, "orFurnitureUpdated");
            return (Criteria) this;
        }

        public Criteria andOrFurnitureUpdatedNotBetween(Date value1, Date value2) {
            addCriterion("or_furniture_updated not between", value1, value2, "orFurnitureUpdated");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
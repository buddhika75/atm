/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package faces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author User
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public void flush() {
        getEntityManager().flush();
    }

    public T findFirstBySQL(String temSQL, Map<String, Object> parameters) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            String pPara = (String) m.getKey();
            if (m.getValue() instanceof Date) {
                Date pVal = (Date) m.getValue();
                qry.setParameter(pPara, pVal, TemporalType.DATE);
//                //System.out.println("Parameter " + pPara + "\tVal" + pVal);
            } else {
                Object pVal = (Object) m.getValue();
                qry.setParameter(pPara, pVal);
//                //System.out.println("Parameter " + pPara + "\tVal" + pVal);
            }
        }
        List<T> l = qry.getResultList();
        if (l != null && l.isEmpty() == false) {
            return l.get(0);
        } else {
            return null;
        }
    }

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
        //getEntityManager().flush();
    }

    public void refresh(T entity) {
        getEntityManager().refresh(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
        //getEntityManager().flush();
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll(boolean withoutRetired) {
        return findAll(null, null, withoutRetired);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(entityClass);
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAll(String fieldName) {
        return findAll(fieldName, "", false);
    }

    public List<T> findAll(String fieldName, boolean withoutRetired) {
        return findAll(fieldName, "", withoutRetired);
    }

    public List<T> findAll(String fieldName, String fieldValue) {
        return findAll(fieldName, fieldValue, false);
    }

    public List<T> findBySQL(String temSQL) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        return qry.getResultList();
    }

    public List<T> findBySQL(String temSQL, int maxResults) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        qry.setMaxResults(maxResults);
        return qry.getResultList();
    }

    public List<T> findBySQL(String temSQL, Map<String, Object> parameters) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
//        //System.out.println("temSQL = " + temSQL);
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            String pPara = (String) m.getKey();
            if (m.getValue() instanceof Date) {
                Date pVal = (Date) m.getValue();
                qry.setParameter(pPara, pVal, TemporalType.DATE);
//                //System.out.println("Parameter " + pPara + "\t Val =" + pVal);
            } else {
                Object pVal = (Object) m.getValue();
                qry.setParameter(pPara, pVal);
//                //System.out.println("Parameter " + pPara + "\t Val =" + pVal);
            }
        }
        return qry.getResultList();
    }

    public List<T> findBySQL(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
            // System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
        return qry.getResultList();
    }

    public List<Object[]> findObjectsArrayBySQL(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<Object[]> qry = getEntityManager().createQuery(temSQL, Object[].class);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
            //    //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
        return qry.getResultList();
    }

    public List<Object> findObjectBySQL(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<Object> qry = getEntityManager().createQuery(temSQL, Object.class);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
            //    //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
        return qry.getResultList();
    }

    public Object[] findObjectListBySQL(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<Object[]> qry = getEntityManager().createQuery(temSQL, Object[].class);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
            //    //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
        return qry.getSingleResult();
    }

    public List<Object> findObjectBySQL(String temSQL) {
        TypedQuery<Object> qry = getEntityManager().createQuery(temSQL, Object.class);
        return qry.getResultList();
    }

    public double findDoubleByJpql(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<Double> qry = (TypedQuery<Double>) getEntityManager().createQuery(temSQL);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
//                //System.out.println("pval is a date");
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
//                //System.out.println("p val is NOT a date");
                qry.setParameter(pPara, pVal);
            }
//            //System.out.println("Parameter " + pPara + "\t and Val\t " + pVal);
        }
        try {
            return (double) qry.getSingleResult();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public long findLongByJpql(String temSQL, Map<String, Object> parameters) {
        return findLongByJpql(temSQL, parameters, TemporalType.DATE);
    }

    public long findLongByJpql(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<Long> qry = (TypedQuery<Long>) getEntityManager().createQuery(temSQL);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
        }
        try {
            return (long) qry.getSingleResult();
        } catch (Exception e) {
            //   System.out.println("e = " + e);
            return 0l;
        }
    }

    public double findDoubleByJpql(String temSQL, Map<String, Object> parameters) {
        return findDoubleByJpql(temSQL, parameters, TemporalType.DATE);
    }

    public List<T> findBySQL(String temSQL, Map<String, Object> parameters, TemporalType tt, int maxRecords) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
//            //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
        qry.setMaxResults(maxRecords);
//        qry.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return qry.getResultList();
    }

    public List<T> findBySQLWithoutCache(String temSQL, Map<String, Object> parameters, TemporalType tt, int maxRecords) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
//            //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
        qry.setMaxResults(maxRecords);
        qry.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return qry.getResultList();
    }

    public List<T> findBySQL(String temSQL, Map<String, Object> parameters, int maxRecords) {
        return findBySQL(temSQL, parameters, TemporalType.DATE, maxRecords);
    }

    private void test(Class myClass, Object ob) {
    }

    public Long countBySql(String sql) {
        return countBySql(sql, null, TemporalType.DATE);
    }

    public Long countBySql(String sql, Map parameters) {
        return countBySql(sql, parameters, TemporalType.DATE);
    }

    public Long countBySql(String sql, Map parameters, TemporalType tt) {
        Query q = getEntityManager().createQuery(sql);
        if (parameters != null) {
            Set s = parameters.entrySet();
            Iterator it = s.iterator();
            while (it.hasNext()) {
                Map.Entry m = (Map.Entry) it.next();
                Object pVal = m.getValue();
                String pPara = (String) m.getKey();
                if (pVal instanceof Date) {
                    Date d = (Date) pVal;
                    q.setParameter(pPara, d, tt);
                } else {
                    q.setParameter(pPara, pVal);
                }
            }
        }
        return (Long) q.getSingleResult();
    }

    public double sumBySql(String sql, Map parameters, TemporalType tt) {
        Query q = getEntityManager().createQuery(sql);
        if (parameters != null) {
            Set s = parameters.entrySet();
            Iterator it = s.iterator();
            while (it.hasNext()) {
                Map.Entry m = (Map.Entry) it.next();
                Object pVal = m.getValue();
                String pPara = (String) m.getKey();
                if (pVal instanceof Date) {
                    Date d = (Date) pVal;
                    q.setParameter(pPara, d, tt);
                } else {
                    q.setParameter(pPara, pVal);
                }
            }
        }
        return (double) q.getSingleResult();
    }

    public Double sumBySql(String sql) {
        Query q = getEntityManager().createQuery(sql);
        return (Double) q.getSingleResult();
    }

    public List<T> findAll(String fieldName, String fieldValue, boolean withoutRetired) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(entityClass);
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        ParameterExpression<String> p = cb.parameter(String.class);
        Predicate predicateField = cb.like(rt.<String>get(fieldName), fieldValue);
        Predicate predicateRetired = cb.equal(rt.<Boolean>get("retired"), false);
        Predicate predicateFieldRetired = cb.and(predicateField, predicateRetired);

        if (withoutRetired && !fieldValue.equals("")) {
            cq.where(predicateFieldRetired);
        } else if (withoutRetired) {
            cq.where(predicateRetired);
        } else if (!fieldValue.equals("")) {
            cq.where(predicateField);
        }

        if (!fieldName.equals("")) {
            cq.orderBy(cb.asc(rt.get(fieldName)));
        }

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findExact(String fieldName, String fieldValue, boolean withoutRetired) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(entityClass);
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        ParameterExpression<String> p = cb.parameter(String.class);
//        Predicate predicateField = cb.like(rt.<String>get(fieldName), fieldValue);
        Predicate predicateField = cb.equal(cb.upper(rt.<String>get(fieldName)), fieldValue.toLowerCase());
        Predicate predicateRetired = cb.equal(rt.<Boolean>get("retired"), false);
        Predicate predicateFieldRetired = cb.and(predicateField, predicateRetired);

        if (withoutRetired && !fieldValue.equals("")) {
            cq.where(predicateFieldRetired);
        } else if (withoutRetired) {
            cq.where(predicateRetired);
        } else if (!fieldValue.equals("")) {
            cq.where(predicateField);
        }

        if (!fieldName.equals("")) {
            cq.orderBy(cb.asc(rt.get(fieldName)));
        }

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findContains(String fieldName, String fieldValue) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(entityClass);
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        ParameterExpression<String> p = cb.parameter(String.class);
//        Predicate predicateField = cb.like(rt.<String>get(fieldName), fieldValue);
        Predicate predicateField = cb.like(cb.upper(rt.<String>get(fieldName)), "*" + fieldValue.toLowerCase());
        //    Predicate predicateRetired = cb.equal(rt.<Boolean>get("retired"), withoutRetired);
        //    Predicate predicateFieldRetired = cb.and(predicateField, predicateRetired);
        //    (cb.like(pet.get(Pet_.name), "*do"));

        if (!fieldValue.equals("")) {
            cq.where(predicateField);
        }

        if (!fieldName.equals("")) {
            cq.orderBy(cb.asc(rt.get(fieldName)));
        }

        return getEntityManager().createQuery(cq).getResultList();
    }

    public T findByField(String fieldName, String fieldValue, boolean withoutRetired) {
        List<T> lstAll = findExact(fieldName, fieldValue, true);

        if (lstAll.isEmpty()) {
//            //System.out.println("Null");
            return null;
        } else {
//            //System.out.println("Not Null " + lstAll.get(0).toString());
            return lstAll.get(0);
        }
    }

    public String findByFieldContains(String fieldName, String fieldValue) {
        List<T> lstAll = findContains(fieldName, fieldValue);

        if (lstAll.isEmpty()) {
//            //System.out.println("Null");
            return "";
        } else {
//            //System.out.println("Not Null " + lstAll.get(0).toString());
            return lstAll.get(0).toString();
        }
    }

    public T findFirstBySQL(String temSQL) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        try {
            return qry.getResultList().get(0);
        } catch (Exception e) {
//            //System.out.println(e.getMessage());
            return null;
        }
    }

    public T findFirstBySQL(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
            //    //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }

        if (!qry.getResultList().isEmpty()) {
            return qry.getResultList().get(0);
        } else {
            return null;
        }
    }

    public <U> List<T> testMethod(U[] a, Collection<U> all) {
        List<T> myList = new ArrayList<T>();
        return myList;
    }

    public <U> List<T> findAll(String fieldName, int searchID, boolean withoutRetired) {

//        final long userId,
//    final long contactNumber){
//
//    final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//    final CriteriaQuery<TaUser> query = cb.createQuery(TaUser.class);
//    final Root<TaUser> root = query.from(TaUser.class);
//    query
//        .where(cb.and(
//            cb.equal(root.get("userId"), userId),
//            cb.equal(root.get("taContact").get("contactNumber"), contactNumber)
//        ));
//    return entityManager.createQuery(query).getSingleResult();
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<T> cq = cb.createQuery(entityClass);
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);

        if (withoutRetired) {
            cq.where(cb.and(cb.equal(rt.get("retired"), false)),
                    (cb.equal(rt.get(fieldName).get("id"), searchID)));
        } else {
            cq.where(cb.equal(rt.get("retired"), false));
        }

        return getEntityManager().createQuery(cq).getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Double findAggregateDbl(String strJQL) {
        Query q = getEntityManager().createQuery(strJQL);
        Double temd;
        try {
            temd = (Double) q.getSingleResult();
            if (temd == null) {
                temd = 0.0;
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            temd = 0.0;
        }
        return temd;
    }

    public Long findAggregateLong(String strJQL) {
        Query q = getEntityManager().createQuery(strJQL);
        Long temd;
        try {
            temd = (Long) q.getSingleResult();
            if (temd == null) {
                temd = 0L;
            }
        } catch (Exception e) {
            temd = 0L;
        }
        return temd;
    }

    public Long findLongByJpql(String strJQL) {
        Query q = getEntityManager().createQuery(strJQL);
        try {
            return (Long) q.getSingleResult();
        } catch (Exception e) {
            return 0l;
        }
    }

    public List<String> findString(String strJQL) {
        Query q = getEntityManager().createQuery(strJQL);
        try {
            return q.getResultList();
        } catch (Exception e) {
//            //System.out.println(e.getMessage());
            return null;
        }
    }

    public List<String> findString(String strJQL, Map map) {
        return findString(strJQL, map, TemporalType.DATE);
    }

    public List<String> findString(String strJQL, Map map, TemporalType tt, int noOfRows) {
        Query q = getEntityManager().createQuery(strJQL);
        Set s = map.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            String pPara = (String) m.getKey();
            if (m.getValue() instanceof Date) {
                Date pVal = (Date) m.getValue();
                q.setParameter(pPara, pVal, tt);
            } else {
                q.setParameter(pPara, m.getValue());
            }
        }
        if (noOfRows != 0) {
            q.setMaxResults(noOfRows);
        }
        try {
            return q.getResultList();
        } catch (Exception e) {
//            //System.out.println(e.getMessage());
            return null;
        }
    }

    public List<String> findString(String strJQL, Map map, TemporalType tt) {
        return findString(strJQL, map, tt, 0);
    }

    public List<Object[]> findAggregates(String temSQL, Map<String, Object> parameters) {
        return findAggregates(temSQL, parameters, TemporalType.DATE);
    }

    public List<Object[]> findAggregates(String temSQL) {
        TypedQuery<Object[]> qry = getEntityManager().createQuery(temSQL, Object[].class);
        try {
            return qry.getResultList();
        } catch (Exception e) {
            //   System.out.println("e = " + e.getMessage());
            return null;
        }
    }

    public List<Object[]> findAggregates(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<Object[]> qry = getEntityManager().createQuery(temSQL, Object[].class);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date pDate = (Date) pVal;
                // qry.setParameter(pPara, pDate, TemporalType.DATE);
                qry.setParameter(pPara, pDate, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
        }
        try {
            return qry.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Object[] findAggregate(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<Object[]> qry = getEntityManager().createQuery(temSQL, Object[].class);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date pDate = (Date) pVal;
                // qry.setParameter(pPara, pDate, TemporalType.DATE);
                qry.setParameter(pPara, pDate, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
        }
        try {
            return qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Object[] findSingleAggregate(String temSQL, Map<String, Object> parameters) {
//        //System.out.println("find aggregates 2" );
        return findSingleAggregate(temSQL, parameters, TemporalType.DATE);
    }

    public Object[] findSingleAggregate(String temSQL, Map<String, Object> parameters, TemporalType tt) {
//        //System.out.println("find aggregates 3");
        TypedQuery<Object[]> qry = getEntityManager().createQuery(temSQL, Object[].class);
//        //System.out.println("2");
        Set s = parameters.entrySet();
//        //System.out.println("m " + parameters);
//        //System.out.println("s = " + s);
//        //System.out.println("3");
        Iterator it = s.iterator();
//        //System.out.println("4");
        while (it.hasNext()) {
//            //System.out.println("5");
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date pDate = (Date) pVal;
                qry.setParameter(pPara, pDate, TemporalType.DATE);
            } else {
                qry.setParameter(pPara, pVal);
            }
//            //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }
//        //System.out.println("6");
        try {
            return qry.getSingleResult();
        } catch (Exception e) {
//            //System.out.println(e.getMessage());
            return null;
        }
    }

    public Double findAggregateDbl(String temSQL, Map<String, Date> parameters) {
        Query qry = getEntityManager().createQuery(temSQL);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();

        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Date pVal = (Date) m.getValue();
            String pPara = (String) m.getKey();
            qry.setParameter(pPara, pVal, TemporalType.DATE);
//            //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }

        try {
            return (Double) qry.getSingleResult();
        } catch (Exception e) {
//            //System.out.println(e.getMessage());
            return 0.0;
        }
    }

    public Long findAggregateLong(String temSQL, Map<String, Object> parameters, TemporalType tt) {
        TypedQuery<Long> qry = getEntityManager().createQuery(temSQL, Long.class);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            Object pVal = m.getValue();
            String pPara = (String) m.getKey();
            if (pVal instanceof Date) {
                Date d = (Date) pVal;
                qry.setParameter(pPara, d, tt);
            } else {
                qry.setParameter(pPara, pVal);
            }
//            //System.out.println("Parameter " + pPara + "\tVal" + pVal);
        }

        try {
            return (Long) qry.getSingleResult();
        } catch (Exception e) {
//            //System.out.println(e.getMessage());
            return 0L;
        }
    }
}

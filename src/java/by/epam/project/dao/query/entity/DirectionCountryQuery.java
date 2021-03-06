package by.epam.project.dao.query.entity;

import by.epam.project.dao.query.generic.GenericDeleteQuery;
import by.epam.project.dao.query.generic.GenericUpdateQuery;
import by.epam.project.dao.query.generic.GenericLoadQuery;
import by.epam.project.dao.query.generic.GenericSaveQuery;
import by.epam.project.dao.query.Criteria;
import static by.epam.project.dao.DaoParamNames.*;
import by.epam.project.dao.query.Appender;
import by.epam.project.dao.query.Params;
import by.epam.project.dao.query.TypedQuery;
import by.epam.project.entity.LinkDirectionCountry;
import by.epam.project.exception.DaoException;
import by.epam.project.exception.DaoQueryException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of direction link to country query forming.
 * @author Helena.Grouk
 */
class DirectionCountryQuery implements TypedQuery<LinkDirectionCountry>{
    private static final String ERR_DIR_COUNTRY_SAVE = "Direction link to country not saved.";
    private static final String ERR_DIR_COUNTRY_LOAD = "Direction link to country not loaded.";
    private static final String ERR_DIR_COUNTRY_UPDATE = "Direction link to country not updated.";
    private static final String ERR_DIR_COUNTRY_DELETE = "Direction link to country not deleted.";
    private static final String WHERE = " where ";
    private static final String AND = " and ";
    private static final String COMMA = " , ";
    private static final String DB_DIRCOUNTRY = "direction_countries";
    private static final String DB_DIRCOUNTRY_ID_COUNTRY = "id_country";
    private static final String DB_DIRCOUNTRY_ID_DIRECTION = "id_direction";
    private static final String SAVE_QUERY =
            "Insert into " + DB_DIRCOUNTRY + " (" + DB_DIRCOUNTRY_ID_DIRECTION + ", "
            + DB_DIRCOUNTRY_ID_COUNTRY + ") values (?, ?);";
    private static final String LOAD_QUERY =
            "Select * from " + DB_DIRCOUNTRY;
    private static final String UPDATE_QUERY =
            "Update " + DB_DIRCOUNTRY + " set ";
    private static final String DELETE_QUERY =
            "Delete from " + DB_DIRCOUNTRY + WHERE;

    @Override
    public List<Integer> save(List<LinkDirectionCountry> beans, GenericSaveQuery saveGeneric, Connection conn) throws DaoQueryException {
        try {
            return saveGeneric.sendQuery(SAVE_QUERY, conn, Params.fill(beans, (LinkDirectionCountry bean) -> {
                Object[] objects = new Object[2];
                objects[0] = bean.getIdDirection();
                objects[1] = bean.getIdCountry();
                return objects;
            }));
        } catch (DaoException ex) {
            throw new DaoQueryException(ERR_DIR_COUNTRY_SAVE, ex);
        }
    }

    @Override
    public List<LinkDirectionCountry> load(Criteria criteria, GenericLoadQuery loadGeneric, Connection conn) throws DaoQueryException {
        int pageSize = 10;

        List paramList = new ArrayList<>();
        StringBuilder sb = new StringBuilder(WHERE);
        String queryStr = new QueryMapper() {
            @Override
            public String mapQuery() {
                Appender.append(DAO_ID_DIRECTION, DB_DIRCOUNTRY_ID_DIRECTION, criteria, paramList, sb, AND);
                Appender.append(DAO_ID_COUNTRY, DB_DIRCOUNTRY_ID_COUNTRY, criteria, paramList, sb, AND);
                if (paramList.isEmpty()) {
                    return LOAD_QUERY;
                } else {
                    return sb.insert(0, LOAD_QUERY).toString();
                }
            }
        }.mapQuery();

        try {
            return loadGeneric.sendQuery(queryStr, paramList.toArray(), pageSize, conn, (ResultSet rs, int rowNum) -> {
                LinkDirectionCountry bean = new LinkDirectionCountry();
                bean.setIdCountry(rs.getInt(DB_DIRCOUNTRY_ID_COUNTRY));
                bean.setIdDirection(rs.getInt(DB_DIRCOUNTRY_ID_DIRECTION));
                return bean;
            });
        } catch (DaoException ex) {
             throw new DaoQueryException(ERR_DIR_COUNTRY_LOAD, ex);
        }
    }

    @Override
    public List<Integer> update(Criteria beans, Criteria criteria, GenericUpdateQuery updateGeneric, Connection conn) throws DaoQueryException {
        List paramList1 = new ArrayList<>();
        List paramList2 = new ArrayList<>();
        StringBuilder sb = new StringBuilder(UPDATE_QUERY);
        String queryStr = new QueryMapper() {
            @Override
            public String mapQuery() {
                Appender.append(DAO_ID_COUNTRY, DB_DIRCOUNTRY_ID_COUNTRY, criteria, paramList1, sb, COMMA);
                sb.append(WHERE);
                Appender.append(DAO_ID_DIRECTION, DB_DIRCOUNTRY_ID_DIRECTION, beans, paramList2, sb, AND);
                return sb.toString();
            }
        }.mapQuery();
        paramList1.addAll(paramList2);

        try {
            return updateGeneric.sendQuery(queryStr, paramList1.toArray(), conn);
        } catch (DaoException ex) {
             throw new DaoQueryException(ERR_DIR_COUNTRY_UPDATE, ex);
        }
    }

    @Override
    public List<Integer> delete(Criteria criteria, GenericDeleteQuery deleteGeneric, Connection conn) throws DaoQueryException {
        List paramList = new ArrayList<>();
        StringBuilder sb = new StringBuilder(DELETE_QUERY);
        String queryStr = new QueryMapper() {
            @Override
            public String mapQuery() {
                Appender.append(DAO_ID_DIRECTION, DB_DIRCOUNTRY_ID_DIRECTION, criteria, paramList, sb, AND);
                Appender.append(DAO_ID_COUNTRY, DB_DIRCOUNTRY_ID_COUNTRY, criteria, paramList, sb, AND);
                return sb.toString();
            }
        }.mapQuery();

        try {
            return deleteGeneric.sendQuery(queryStr, paramList.toArray(), conn);
        } catch (DaoException ex) {
             throw new DaoQueryException(ERR_DIR_COUNTRY_DELETE, ex);
        }
    }
}


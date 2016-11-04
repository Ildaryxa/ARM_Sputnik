package model.modelOject;

import orm.DbHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 05.07.2016.
 */
public class Graph {
    List<String> title;
    String[][] links;

    public static class Link {
        String table1;
        String table2;
        String field1;
        String field2;

        public Link(String table1, String field1, String table2, String field2) {
            this.table1 = table1;
            this.table2 = table2;
            this.field1 = field1;
            this.field2 = field2;
        }
    }

    public static String getTableLinkString =
            "SELECT \n" +
                    "\tTABLE_NAME,\n" +
                    "\tCOLUMN_NAME,\n" +
                    "\tREFERENCED_TABLE_NAME,\n" +
                    "\tREFERENCED_COLUMN_NAME\n" +
                    "FROM \n" +
                    "\tinformation_schema.KEY_COLUMN_USAGE\n" +
                    "WHERE \n" +
                    "\tCONSTRAINT_SCHEMA = 'sputnik' AND\n" +
                    "\tNOT isNULL(REFERENCED_TABLE_NAME);";

    public Graph() {
        title = new ArrayList<>();
    }

    public Graph(List<String> title, String[][] links) {
        this.title = title;
        this.links = links;
    }

    public static Graph build() throws SQLException {
        DbHelper db = DbHelper.INSTANCE;
        db.openConnection();
        ResultSet resultSet = db.sendGet(getTableLinkString);

        List<Link> links = new ArrayList<>();
        while (resultSet.next()) links.add(
                new Link(resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)));
        db.closeConnection();
        return build(links);
    }

    public static Graph build(List<Link> list) {
        Graph graph = new Graph();

        for (Link link : list) {
            if (!graph.title.contains(link.table1))
                graph.title.add(link.table1);
            if (!graph.title.contains(link.table2))
                graph.title.add(link.table2);
        }

        int n = graph.title.size();
        graph.links = new String[n][n];

        for (Link link : list) {
            int i = graph.title.indexOf(link.table1);
            int j = graph.title.indexOf(link.table2);
            String content = link.table1 + "." + link.field1 + " = " + link.table2 + "." + link.field2;
            graph.links[i][j] = content;
            graph.links[j][i] = content;
        }
        return graph;
    }

    public Graph getLinkTableList(List<String> tableList) {
        int n = links.length;
        Graph graph = new Graph(title, new String[n][n]);
        for (int i = 0; i < tableList.size(); i++) {
            for (int j = i+1; j < tableList.size(); j++) {
                if (i == j || graph.existPath(this, tableList.get(i), tableList.get(j))) continue;

            }
        }
        System.out.println("done");
        return graph;
    }

    private boolean existPath(Graph graph, String t1, String t2) {
        Map<String, Boolean> useTables = new HashMap<>();
        for (String t : title) {
            useTables.put(t, false);
        }
        useTables.put(t1, true);
        useTables.put(t2, true);
        return existPath(useTables, graph, t1, t2);
    }

    private boolean existPath(Map<String, Boolean> useTable, Graph graph, String t1, String t2) {
        int i = title.indexOf(t1);
        int j = title.indexOf(t2);

        if (graph.links[i][j] != null) {
            return true;
        } else

        for (int k = 0; k < useTable.size(); k++) {
            if (!useTable.get(title.get(k)) && graph.links[i][k] != null) {
                useTable.put(title.get(k), true);
                if (existPath(useTable, graph, t1, title.get(k)) &&
                        existPath(useTable, graph, t2, title.get(k))) {
                    links[i][k] = graph.getLinks()[i][k];
                    links[k][i] = graph.getLinks()[k][i];
                    links[j][k] = graph.getLinks()[j][k];
                    links[k][j] = graph.getLinks()[k][j];
                }
                if (existPath(useTable, graph, t1, title.get(k))) {
                    links[i][k] = graph.getLinks()[i][k];
                    links[k][i] = graph.getLinks()[i][k];
                    if (existPath(useTable, graph, title.get(k), t2)) {
                        return true;
                    } else {
                        links[i][k] = null;
                        links[k][i] = null;
                    }
                }
            }

            if (!useTable.get(title.get(k)) && graph.links[j][k] != null) {
                useTable.put(title.get(k), true);
                if (existPath(useTable, graph, t2, title.get(k)) &&
                        existPath(useTable, graph, t1, title.get(k))) {
                    links[j][k] = graph.getLinks()[j][k];
                    links[k][j] = graph.getLinks()[k][j];
                    links[i][k] = graph.getLinks()[i][k];
                    links[k][i] = graph.getLinks()[k][i];
                }
                if (existPath(useTable, graph, t2, title.get(k))) {
                    links[j][k] = graph.getLinks()[j][k];
                    links[k][j] = graph.getLinks()[j][k];
                    if (existPath(useTable, graph, title.get(k), t1)) {
                        return true;
                    } else {
                        links[j][k] = null;
                        links[k][j] = null;
                    }
                }
            }
            /*
            if (!useTable.get(title.get(k)) && graph.links[j][k] != null) {
                if (existPath(useTable, graph, t2, title.get(k))) {
                    links[j][k] = graph.getLinks()[j][k];
                    links[k][j] = graph.getLinks()[j][k];
                    if (existPath(useTable, graph, title.get(k), t1)) {
                        return true;
                    } else {
                        links[j][k] = null;
                    }
                }
                useTable.put(title.get(k), false);
            }*/
        }
        return false;

    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public String[][] getLinks() {
        return links;
    }

    public void setLinks(String[][] links) {
        this.links = links;
    }
}

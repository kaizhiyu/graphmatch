package com.fudanse.graphmatch.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alibaba.fastjson.JSON;
import com.fudanse.graphmatch.model.Edge;
import com.fudanse.graphmatch.model.NeoNode;
import com.fudanse.graphmatch.util.CypherStatment;
import com.fudanse.graphmatch.util.DBUtil;

public class NeoDAO implements INeoDAO {

	@Override
	public NeoNode saveNeoNode(NeoNode node) {
		NeoNode returnNode = null;
		try {
			Connection con = DBUtil.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(CypherStatment.getInsertCypher(node));
			if (rs.next()) {
				String json = rs.getString("n");
				String str[] = json.split("\"name\":");
				str[1] = str[1].replaceAll("\"", "\'");
				json = str[0] + "\"name\":\"" + str[1].substring(1, str[1].length() - 2) + "\"}";
				returnNode = JSON.parseObject(json, NeoNode.class);
			}
			DBUtil.closeResultset(rs);
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		node = returnNode;
		return node;
	}

	@Override
	public void saveEdge(NeoNode left, NeoNode right, Edge e) {
		try {
			Connection con = DBUtil.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeQuery(CypherStatment.getInsertCypher(left, right, e));
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(con);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public NeoNode addLabel(Integer id, String label) {
		NeoNode returnNode = null;
		try {
			Connection con = DBUtil.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeQuery(CypherStatment.getAddLabelCypher(id, label));
			
			DBUtil.closeStatement(stmt);
			DBUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnNode;
	}

	/*
	 * public static void main(String[] args) { INeoDAO nd = new NeoDAO();
	 * 
	 * nd.addLabel(72, "aaa"); }
	 */

}

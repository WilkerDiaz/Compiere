package org.compiere.db;
import java.util.ArrayList;


public class QueryWithBindings
{
	public final String sql;
	public final ArrayList< Object > params;

	public QueryWithBindings( String sql, ArrayList< Object > params )
	{
		this.sql = sql;
		this.params = params;
	}
}


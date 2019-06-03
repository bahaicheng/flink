package org.apache.flink.streaming.examples.access.avro;

import java.io.Serializable;

public class ExportData	implements Serializable {

	/**
	 * before : {"id":3401,"user_id":1,"name":"kafka connect","description":null,"stream_info":"","client_info":"","thumbnail_info":null,"gmt_create":1556633639000,"gmt_modified":1556638780000,"is_deleted":false}
	 * source : {"version":"0.9.4.Final","connector":"mysql","name":"dbserver4","server_id":223344,"ts_sec":1556609982,"gtid":null,"file":"mysql-bin.000002","pos":454241,"row":0,"snapshot":false,"thread":333,"db":"appmgr","table":"application","query":null}
	 * after : {"id":3401,"user_id":1,"name":"kafka connect","description":null,"stream_info":"","client_info":"","thumbnail_info":null,"gmt_create":1556633639000,"gmt_modified":1556638782000,"is_deleted":false}
	 * op : u
	 * ts_ms : 1556609982467
	 */

	private BeforeBean before;
	private SourceBean source;
	private AfterBean after;
	private String op;
	private long ts_ms;

	public BeforeBean getBefore() {
		return before;
	}

	public void setBefore(BeforeBean before) {
		this.before = before;
	}

	public SourceBean getSource() {
		return source;
	}

	public void setSource(SourceBean source) {
		this.source = source;
	}

	public AfterBean getAfter() {
		return after;
	}

	public void setAfter(AfterBean after) {
		this.after = after;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public long getTs_ms() {
		return ts_ms;
	}

	public void setTs_ms(long ts_ms) {
		this.ts_ms = ts_ms;
	}

	public static class BeforeBean {
		/**
		 * id : 3401
		 * user_id : 1
		 * name : kafka connect
		 * description : null
		 * stream_info :
		 * client_info :
		 * thumbnail_info : null
		 * gmt_create : 1556633639000
		 * gmt_modified : 1556638780000
		 * is_deleted : false
		 */

		private int id;
		private int user_id;
		private String name;
		private Object description;
		private String stream_info;
		private String client_info;
		private Object thumbnail_info;
		private long gmt_create;
		private long gmt_modified;
		private boolean is_deleted;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getDescription() {
			return description;
		}

		public void setDescription(Object description) {
			this.description = description;
		}

		public String getStream_info() {
			return stream_info;
		}

		public void setStream_info(String stream_info) {
			this.stream_info = stream_info;
		}

		public String getClient_info() {
			return client_info;
		}

		public void setClient_info(String client_info) {
			this.client_info = client_info;
		}

		public Object getThumbnail_info() {
			return thumbnail_info;
		}

		public void setThumbnail_info(Object thumbnail_info) {
			this.thumbnail_info = thumbnail_info;
		}

		public long getGmt_create() {
			return gmt_create;
		}

		public void setGmt_create(long gmt_create) {
			this.gmt_create = gmt_create;
		}

		public long getGmt_modified() {
			return gmt_modified;
		}

		public void setGmt_modified(long gmt_modified) {
			this.gmt_modified = gmt_modified;
		}

		public boolean isIs_deleted() {
			return is_deleted;
		}

		public void setIs_deleted(boolean is_deleted) {
			this.is_deleted = is_deleted;
		}
	}

	public static class SourceBean {
		/**
		 * version : 0.9.4.Final
		 * connector : mysql
		 * name : dbserver4
		 * server_id : 223344
		 * ts_sec : 1556609982
		 * gtid : null
		 * file : mysql-bin.000002
		 * pos : 454241
		 * row : 0
		 * snapshot : false
		 * thread : 333
		 * db : appmgr
		 * table : application
		 * query : null
		 */

		private String version;
		private String connector;
		private String name;
		private int server_id;
		private int ts_sec;
		private Object gtid;
		private String file;
		private int pos;
		private int row;
		private boolean snapshot;
		private int thread;
		private String db;
		private String table;
		private Object query;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getConnector() {
			return connector;
		}

		public void setConnector(String connector) {
			this.connector = connector;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getServer_id() {
			return server_id;
		}

		public void setServer_id(int server_id) {
			this.server_id = server_id;
		}

		public int getTs_sec() {
			return ts_sec;
		}

		public void setTs_sec(int ts_sec) {
			this.ts_sec = ts_sec;
		}

		public Object getGtid() {
			return gtid;
		}

		public void setGtid(Object gtid) {
			this.gtid = gtid;
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		public int getPos() {
			return pos;
		}

		public void setPos(int pos) {
			this.pos = pos;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public boolean isSnapshot() {
			return snapshot;
		}

		public void setSnapshot(boolean snapshot) {
			this.snapshot = snapshot;
		}

		public int getThread() {
			return thread;
		}

		public void setThread(int thread) {
			this.thread = thread;
		}

		public String getDb() {
			return db;
		}

		public void setDb(String db) {
			this.db = db;
		}

		public String getTable() {
			return table;
		}

		public void setTable(String table) {
			this.table = table;
		}

		public Object getQuery() {
			return query;
		}

		public void setQuery(Object query) {
			this.query = query;
		}
	}

	public static class AfterBean {
		/**
		 * id : 3401
		 * user_id : 1
		 * name : kafka connect
		 * description : null
		 * stream_info :
		 * client_info :
		 * thumbnail_info : null
		 * gmt_create : 1556633639000
		 * gmt_modified : 1556638782000
		 * is_deleted : false
		 */

		private int id;
		private int user_id;
		private String name;
		private Object description;
		private String stream_info;
		private String client_info;
		private Object thumbnail_info;
		private long gmt_create;
		private long gmt_modified;
		private boolean is_deleted;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getDescription() {
			return description;
		}

		public void setDescription(Object description) {
			this.description = description;
		}

		public String getStream_info() {
			return stream_info;
		}

		public void setStream_info(String stream_info) {
			this.stream_info = stream_info;
		}

		public String getClient_info() {
			return client_info;
		}

		public void setClient_info(String client_info) {
			this.client_info = client_info;
		}

		public Object getThumbnail_info() {
			return thumbnail_info;
		}

		public void setThumbnail_info(Object thumbnail_info) {
			this.thumbnail_info = thumbnail_info;
		}

		public long getGmt_create() {
			return gmt_create;
		}

		public void setGmt_create(long gmt_create) {
			this.gmt_create = gmt_create;
		}

		public long getGmt_modified() {
			return gmt_modified;
		}

		public void setGmt_modified(long gmt_modified) {
			this.gmt_modified = gmt_modified;
		}

		public boolean isIs_deleted() {
			return is_deleted;
		}

		public void setIs_deleted(boolean is_deleted) {
			this.is_deleted = is_deleted;
		}
	}
}

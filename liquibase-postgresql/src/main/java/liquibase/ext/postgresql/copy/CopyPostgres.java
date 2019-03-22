package liquibase.ext.postgresql.copy;

public class CopyPostgres { // extends AbstractSqlGenerator<CopyStatement> {
//
//	@Override
//	public boolean supports(CopyStatement copyStatement, Database database) {
//        return database instanceof PostgresDatabase;
//    }
//
//	@Override
//	public ValidationErrors validate(CopyStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
//		return new ValidationErrors();
//	}
//
//	@Override
//	public Sql[] generateSql(CopyStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
//
//		List<Sql> ret = new ArrayList<Sql>();
//		String sql = "";
//
//		try {
//			sql += "COPY ";
//			sql += database.escapeTableName(statement.getCatalogName(),
//					statement.getSchemaName(), statement.getTableName());
//			sql += " FROM STDIN;" + "\n";
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(statement.getInputStream()));
//	        StringBuilder out = new StringBuilder();
//	        String line;
//	        while ((line = reader.readLine()) != null) {
//	            out.append(line);
//	        }
//
//	        sql += "\\."; // End with \. for textual inputs
//			sql += out.toString();
//			ret.add(new UnparsedSql(sql));
//		} catch (IOException e) {
//			// TODO: handle exception
//			// Don't know how to handle this kind of problem ...
//		}
//
//		return ret.toArray(new Sql[ret.size()]);
//	}

}

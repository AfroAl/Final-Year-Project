/* Parameterised String
String query = "INSERT INTO Quotes (id, content) VALUES ('" + quote.getId().toString() + "', '" + quote.getContent() + "')";
Statement statement = connection.createStatement();
statement.execute(query);
statement.close();
*/
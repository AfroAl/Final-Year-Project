/* LDAP Injection
String filter = "(&(uid={0})(userPassword={1}))";
NamingEnumeration<SearchResult> results = ctx.search("ou=system", filter, new String[]{user, pass}, new SearchControls());

return results.hasMore();
 */

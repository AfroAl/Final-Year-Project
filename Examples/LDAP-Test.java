/* LDAP Injection
String filter = "(&(uid=" + user + ")(userPassword=" + pass + "))";

NamingEnumeration<SearchResult> results = ctx.search("ou=system", filter, new SearchControls());
return results.hasMore();
 */
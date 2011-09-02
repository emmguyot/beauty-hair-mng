// IntHashtable - a Hashtable that uses ints as the keys
//
// This is 90% based on JavaSoft's java.util.Hashtable.
//
// Visit the ACME Labs Java page for up-to-date versions of this and other
// fine Java utilities: http://www.acme.com/java/

package Acme;

import java.util.*;

class IntHashtableEnumerator implements Enumeration
	{
	boolean keys;
	int index;
	IntHashtableEntry table[];
	IntHashtableEntry entry;

	IntHashtableEnumerator( IntHashtableEntry table[], boolean keys )
	{
	this.table = table;
	this.keys = keys;
	this.index = table.length;
	}
	
	public boolean hasMoreElements()
	{
	if ( entry != null )
	    return true;
	while ( index-- > 0 )
	    if ( ( entry = table[index] ) != null )
		return true;
	return false;
	}

	public Object nextElement()
	{
	if ( entry == null )
	    while ( ( index-- > 0 ) && ( ( entry = table[index] ) == null ) )
		;
	if ( entry != null )
	    {
	    IntHashtableEntry e = entry;
	    entry = e.next;
	    return keys ? new Integer( e.key ) : e.value;
	    }
	throw new NoSuchElementException( "IntHashtableEnumerator" );
	}
    }

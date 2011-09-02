// IntHashtable - a Hashtable that uses ints as the keys
//
// This is 90% based on JavaSoft's java.util.Hashtable.
//
// Visit the ACME Labs Java page for up-to-date versions of this and other
// fine Java utilities: http://www.acme.com/java/

package Acme;

import java.util.*;

class IntHashtableEntry
	{
	int hash;
	int key;
	Object value;
	IntHashtableEntry next;

	protected Object clone()
	{
	IntHashtableEntry entry = new IntHashtableEntry();
	entry.hash = hash;
	entry.key = key;
	entry.value = value;
	entry.next = ( next != null ) ? (IntHashtableEntry) next.clone() : null;
	return entry;
	}
    }



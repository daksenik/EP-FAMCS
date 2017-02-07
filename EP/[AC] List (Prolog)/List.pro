/*****************************************************************************

		Copyright (c) My Company

 Project:  LIST
 FileName: LIST.PRO
 Purpose: No description
 Written by: Visual Prolog
 Comments:
******************************************************************************/
domains
	list = integer*

include "list.inc"

predicates

	shuffle(list,list,list)
	length(list,integer)
	addElement(list,list,list,integer)
	
clauses
	length([], 0).
	length([_|T], N) :- length(T, N1), N = N1 + 1.
	
	addElement([X|L1],L2,[X|L3],0):-addElement(L1,L2,L3,1),not(length(L2,0)).
	addElement(L1,[Y|L2],[Y|L3],1):-addElement(L1,L2,L3,0),not(length(L1,0)).	
	addElement([],[Y|L2],[Y|L3],D):-addElement([],L2,L3,D).
	addElement([X|L1],[],[X|L3],D):-addElement(L1,[],L3,D).
	addElement([],[],[],_).
	
	shuffle([],[],[]).
	shuffle(L1,L2,L3):-addElement(L1,L2,L3,0).
	shuffle([],L2,L2).
	shuffle(L1,[],L1).

goal

	shuffle([],[],L3).

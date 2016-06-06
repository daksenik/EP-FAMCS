/*****************************************************************************

		Copyright (c) My Company

 Project:  GRAPH
 FileName: GRAPH.PRO
 Purpose: No description
 Written by: Visual Prolog
 Comments:
******************************************************************************/

include "graph.inc"

domains 
	myInteger = integer
	point = point(myInteger,myInteger)
	list = point*

predicates

	white(point)
	isExit(point)
	searchExit(point,list,list)
	
	notContains(point,list)
	
	findPaths(list)
	firstElement(point,list)
clauses
	isExit(point(1,3)).
	
	white(point(1,1)).
	white(point(1,3)).
	white(point(2,1)).
	white(point(2,2)).
%	white(point(2,3)).	
	white(point(3,1)).
	white(point(3,2)).
	white(point(3,3)).	
	
	firstElement(point(X,Y),[point(X,Y)|_]).

	findPaths(Out):-searchExit(point(1,1),[point(1,1)],Out).

	notContains(point(X,Y),[]).	
	notContains(point(X,Y),[point(A,B)|L]):-notContains(point(X,Y),L),X<>A,Y<>B.
	notContains(point(X,Y),[point(X,B)|L]):-notContains(point(X,Y),L),Y<>B.
	notContains(point(X,Y),[point(A,Y)|L]):-notContains(point(X,Y),L),X<>A.	
	
	searchExit(point(X,Y),L,L):-isExit(point(X,Y)).
	
	searchExit(point(X,Y),L,Out):-
		white(point(X,Y)),
		YP1 = Y+1,
		white(point(X,YP1)),
		notContains(point(X,YP1),L),
		L1 = [point(X,YP1)|L],
		searchExit(point(X,YP1),L1,Out).
		
	searchExit(point(X,Y),L,Out):-
		white(point(X,Y)),
		YM1 = Y-1,
		white(point(X,YM1)),
		notContains(point(X,YM1),L),
		L1 = [point(X,YM1)|L],
		searchExit(point(X,YM1),L1,Out).
		
	searchExit(point(X,Y),L,Out):-
		white(point(X,Y)),
		XP1 = X+1,
		white(point(XP1,Y)),
		notContains(point(XP1,Y),L),
		L1 = [point(XP1,Y)|L],
		searchExit(point(XP1,Y),L1,Out).
		
	searchExit(point(X,Y),L,Out):-
		white(point(X,Y)),
		XM1 = X-1,
		white(point(XM1,Y)),
		notContains(point(XM1,Y),L),
		L1 = [point(XM1,Y)|L],
		searchExit(point(XM1,Y),L1,Out).		
					
goal
	findPaths(L).
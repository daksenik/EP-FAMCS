/*****************************************************************************

		Copyright (c) My Company

 Project:  SUMM
 FileName: SUMM.PRO
 Purpose: No description
 Written by: Visual Prolog
 Comments:
******************************************************************************/

include "summ.inc"

predicates
	nondeterm summ(integer,real)
	nondeterm sum1(integer,integer,real,real,real,real)
clauses
	summ(N,Out):-
		_M1 = N-1,
		_M2 = N,
		_Cur = N*(N-1),
		sum1(1,N,_M1,_M2,_Cur,Out).
	
	sum1(1,1,_,_,_,1).
	
	sum1(N,N,_,_,S,S):-N>1.
	sum1(K,N,M1,M2,Cur,Out):-N>1,K<N,
		_K = K+1,
		_N2 = N/2,
		_K<_N2,
		_M1 = M1*(N-2*_K+1)*(N-2*_K+2)/(_K*(N-_K+1)),
		_M2 = M2*(N-_K+1)/(_K*_K),
		_Cur = Cur+_M1*_M2,
		sum1(_K,N,_M1,_M2,_Cur,Out).
	sum1(K,N,_,M2,Cur,Out):-N>1,K<N,
		_K = K+1,
		_N2 = N/2,
		_K>=_N2,		
		_M2 = M2*(N-_K+1)/(_K*_K),
		_Cur = Cur+_M2,
		sum1(_K,N,1,_M2,_Cur,Out).

goal
	summ(100,Out).
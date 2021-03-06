/*****************************************************************************

		Copyright (c) My Company

 Project:  PARENT
 FileName: PARENT.PRO
 Purpose: No description
 Written by: Visual Prolog
 Comments:
******************************************************************************/

include "parent.inc"

domains
  list = string*

predicates

  parent(string,string)
  man(string)
  woman(string)
  brothers(string,string,string)
  count(string)  
  threeBrothers(string)  
  
clauses

  parent("Lena","Tanya").
  parent("Lena","Semyon").
  parent("Oleg","Tanya").
  parent("Oleg","Semyon").
  parent("Tanya","Pyotr").
  parent("Semyon","Irina").
  parent("Semyon","Dima").
  parent("Semyon", "Ilya").
  parent("Anna","Andrey").
  parent("Anna","Dima").
%  parent("Semyon","Kolya").
  parent("Pyotr","X").
  parent("Pyotr","Y").
  parent("Pyotr","Z").  
  
  man("Andrey").
  man("X").
  man("Y").
  man("Z").
  man("Ilya").
  man("Semyon").
  man("Oleg").
  man("Pyotr").
  man("Dima").
  man("Kolya").
  
  woman("Lena").
  woman("Tanya").
  woman("Irina").
  

  count(Name):-not(threeBrothers(Name)).
  threeBrothers(X):-parent(C,X),parent(C,Y),parent(C,Z),parent(C,N),man(Y),man(Z),man(N),X<>Y,X<>Z,X<>N,Y<>Z,Y<>N,Z<>N.
  threeBrothers(X):-parent(C,X),parent(C,Y),parent(C,Z),parent(D,N),parent(D,X),man(Y),man(Z),man(N),X<>Y,X<>Z,X<>N,Y<>Z,Y<>N,Z<>N.  
  
  brothers(X,Y,Z):-parent(C,X),parent(C,Y),parent(C,Z),man(Y),man(Z),X<>Y,X<>Z,Y<>Z,count(X).
  brothers(X,Y,Z):-parent(C,X),parent(C,Y),parent(K,Z),parent(K,X),man(Y),man(Z),X<>Y,X<>Z,Y<>Z,K<>C,count(X).  
goal

brothers(X,Y,Z).

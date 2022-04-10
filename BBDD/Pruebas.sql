USE MyVideoGameList
INSERT INTO Usuarios(Id) VALUES
	('aa'),
	('bb')

INSERT INTO ListaVideojuegos(IdUsuario, IdVideojuego, FechaDeComienzo, FechaDeFinalizacion, Nota, Dificultad, Estado) VALUES
	('aa',1,'11/01/2022', '27/03/2022', 7, 5, 1)
INSERT INTO ListaVideojuegos(IdUsuario, IdVideojuego, FechaDeComienzo, FechaDeFinalizacion, Nota, Dificultad, Estado) VALUES
	('aa',2,'11/01/2022', '27/03/2022', 3, 2, 1)
INSERT INTO ListaVideojuegos(IdUsuario, IdVideojuego, FechaDeComienzo, FechaDeFinalizacion, Nota, Dificultad, Estado) VALUES
	('bb',2,'11/01/2022', '27/03/2022', 6, 1, 1)
INSERT INTO ListaVideojuegos(IdUsuario, IdVideojuego, FechaDeComienzo, FechaDeFinalizacion, Nota, Dificultad, Estado) VALUES
	('bb',4,'11/01/2022', '27/03/2022', 6, 1, 2)
INSERT INTO ListaVideojuegos(IdUsuario, IdVideojuego, FechaDeComienzo, FechaDeFinalizacion, Nota, Dificultad, Estado) VALUES
	('bb',5,'11/01/2022', '27/03/2022', 6, 1, 5)
SELECT * FROM Videojuegos
SELECT * FROM ListaVideojuegos
UPDATE ListaVideojuegos
SET Nota = 10
WHERE IdUsuario = 'aa' AND IdVideojuego = 2
UPDATE ListaVideojuegos
SET Dificultad = 10
WHERE IdUsuario = 'aa' AND IdVideojuego = 2
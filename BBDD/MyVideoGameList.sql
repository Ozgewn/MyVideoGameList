CREATE DATABASE MyVideoGameList
GO
USE MyVideoGameList
GO

CREATE TABLE Usuarios(
	Id int IDENTITY(1,1) Not NULL CONSTRAINT PK_Usuarios PRIMARY KEY
	,Nickname varChar(15) Not NULL
	,UserPassword varChar(20) Not NULL
)
CREATE TABLE Videojuegos(
	Id int IDENTITY(1,1) Not NULL CONSTRAINT PK_Videojuegos PRIMARY KEY
	,Nombre varChar(35) Not NULL
	,Desarrollador varChar(50) Not NULL
	,Distribuidores varChar(50) Not NULL
	,Plataformas varChar(60) Not NULL
	,NotaMedia float NULL
	,DificultadMedia float NULL
	,FechaDeLanzamiento DateTime Not NULL
	,Generos varChar(50) Not NULL
)
CREATE TABLE ListaVideojuegos(
	IdUsuario int Not NULL CONSTRAINT FK_Usuarios FOREIGN KEY REFERENCES Usuarios(Id)
	,IdVideojuego int Not NULL CONSTRAINT FK_Videojuegos FOREIGN KEY REFERENCES Videojuegos(Id)
	,FechaDeComienzo DateTime NULL
	,FechaDeFinalizacion DateTime NULL
	,Nota float NULL
	,Dificultad float NULL
	,CONSTRAINT PK_ListaVideojuegos PRIMARY KEY (IdUsuario,IdVideojuego)
)
GO
CREATE OR ALTER TRIGGER Calcular_NotaMedia ON ListaVideojuegos AFTER INSERT AS
--TRIGGER PARA CALCULAR LA NOTA MEDIA CADA VEZ QUE SE INSERTE UN DATO EN LA LA TABLA DE LA LISTA DE VIDEOJUEGOS
DECLARE @Media FLOAT
DECLARE @NuevoIdVideojuego INT

SELECT @NuevoIdVideojuego = IdVideojuego FROM inserted

SELECT @Media = AVG(Nota) FROM ListaVideojuegos
	WHERE IdVideojuego = @NuevoIdVideojuego

	UPDATE Videojuegos
		SET NotaMedia = @Media FROM ListaVideojuegos AS LV
GO
CREATE OR ALTER TRIGGER Calcular_NotaMediaBorrado ON ListaVideojuegos AFTER DELETE AS
--TRIGGER PARA CALCULAR LA NOTA MEDIA CADA VEZ QUE SE BORRE UN DATO EN LA LA TABLA DE LA LISTA DE VIDEOJUEGOS
DECLARE @Media FLOAT
DECLARE @AntiguoIdVideojuego INT
DECLARE @Count INT

SELECT @AntiguoIdVideojuego = IdVideojuego FROM deleted

SET @Count = (SELECT COUNT(*) FROM ListaVideojuegos
	WHERE IdVideojuego = @AntiguoIdVideojuego)

SET @Media = 0

IF @Count >= 1
	BEGIN
		SELECT @Media = ISNULL(AVG(Nota),NULL) FROM ListaVideojuegos
			WHERE IdVideojuego = @AntiguoIdVideojuego
	END

	UPDATE Videojuegos
		SET NotaMedia = @Media
			WHERE Id = @AntiguoIdVideojuego
GO
CREATE OR ALTER TRIGGER Calcular_DificultadMedia ON ListaVideojuegos AFTER INSERT AS
--TRIGGER PARA CALCULAR LA DIFICULTAD MEDIA CADA VEZ QUE SE INSERTE UN DATO EN LA LA TABLA DE LA LISTA DE VIDEOJUEGOS
DECLARE @Media FLOAT
DECLARE @NuevoIdVideojuego INT

SELECT @NuevoIdVideojuego = IdVideojuego FROM inserted

SELECT @Media = AVG(Dificultad) FROM ListaVideojuegos
	WHERE IdVideojuego = @NuevoIdVideojuego

	UPDATE Videojuegos
		SET DificultadMedia = @Media FROM ListaVideojuegos AS LV
GO
CREATE OR ALTER TRIGGER Calcular_DificultadMediaBorrado ON ListaVideojuegos AFTER DELETE AS
--TRIGGER PARA CALCULAR LA DIFICULTAD MEDIA CADA VEZ QUE SE BORRE UN DATO EN LA LA TABLA DE LA LISTA DE VIDEOJUEGOS
DECLARE @Media FLOAT
DECLARE @AntiguoIdVideojuego INT
DECLARE @Count INT

SELECT @AntiguoIdVideojuego = IdVideojuego FROM deleted

SET @Count = (SELECT COUNT(*) FROM ListaVideojuegos
	WHERE IdVideojuego = @AntiguoIdVideojuego)

SET @Media = 0

IF @Count >= 1
	BEGIN
		SELECT @Media = ISNULL(AVG(Dificultad),NULL) FROM ListaVideojuegos
			WHERE IdVideojuego = @AntiguoIdVideojuego
	END

	UPDATE Videojuegos
		SET DificultadMedia = @Media
			WHERE Id = @AntiguoIdVideojuego
GO
INSERT INTO Usuarios(Nickname, UserPassword) VALUES('Prueba123', 'Constrasenya123')
INSERT INTO Usuarios(Nickname, UserPassword) VALUES('Prueba321', 'Constrasenya321')
INSERT INTO Videojuegos(Nombre, Desarrollador, Distribuidores, Plataformas, FechaDeLanzamiento, Generos) VALUES('The Witcher 3', 'CD Projekt RED', 'Warner Bros, Namco Bandai Games', 'PC, XboxONE, Series X y Series Y, PS4, PS5, Nintendo Switch', '19-05-2015', 'ARPG')
INSERT INTO ListaVideojuegos (IdUsuario, IdVideojuego, FechaDeComienzo, FechaDeFinalizacion, Nota, Dificultad) VALUES(1,1,CURRENT_TIMESTAMP, NULL, 9, 2)
INSERT INTO ListaVideojuegos (IdUsuario, IdVideojuego, FechaDeComienzo, FechaDeFinalizacion, Nota, Dificultad) VALUES(2,1,CURRENT_TIMESTAMP, NULL, 4, 3)
SELECT * FROM ListaVideojuegos
SELECT * FROM Videojuegos
DELETE FROM ListaVideojuegos where IdUsuario = 1
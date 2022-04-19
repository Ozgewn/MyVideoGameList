using MVGL_Entidades;

namespace MVGL_API.Models
{
    public class clsListaConInfoDeVideojuego : clsListaVideojuego
    {
        //Esta clase es una clase que une la entidad listavideojuegos y la entidad videojuegos, es decir, es una clase que tiene los datos del videojuego (como el nombre, la imagen, la nota media), aparte
        //de la propia informacion del registro de la lista, pero solo cogemos cierta informacion del videojuego, esto con el objetivo de mostrarlo en una lista, la cual tendra la siguiente info:
        //La nota que le tienes tu, la nota media, la imagen, el nombre, la dificultad que le tienes puesta tú en tu lista, la dificultad media, etc.

        private string urlImagenVideojuego, nombreVideojuego, generos;
        private double notaMediaVideojuego, dificultadMediaVideojuego;

        public clsListaConInfoDeVideojuego(): base()
        {

        }

        public clsListaConInfoDeVideojuego(clsListaVideojuego oInfoVideojuegoEnLista, clsVideojuego oVideojuegoEnRegistro) : base(oInfoVideojuegoEnLista.IdUsuario, oInfoVideojuegoEnLista.IdVideojuego, oInfoVideojuegoEnLista.FechaDeComienzo, oInfoVideojuegoEnLista.FechaDeFinalizacion, oInfoVideojuegoEnLista.Nota, oInfoVideojuegoEnLista.Dificultad, oInfoVideojuegoEnLista.Estado)
        {
            urlImagenVideojuego = oVideojuegoEnRegistro.UrlImagen;
            nombreVideojuego = oVideojuegoEnRegistro.Nombre;
            notaMediaVideojuego = oVideojuegoEnRegistro.NotaMedia;
            dificultadMediaVideojuego = oVideojuegoEnRegistro.DificultadMedia;
            generos = oVideojuegoEnRegistro.Generos;
        }

        public clsListaConInfoDeVideojuego(clsVideojuego oVideojuegoEnRegistro) : base()
        {
            IdVideojuego = oVideojuegoEnRegistro.Id;
            urlImagenVideojuego = oVideojuegoEnRegistro.UrlImagen;
            nombreVideojuego = oVideojuegoEnRegistro.Nombre;
            notaMediaVideojuego = oVideojuegoEnRegistro.NotaMedia;
            dificultadMediaVideojuego = oVideojuegoEnRegistro.DificultadMedia;
            generos = oVideojuegoEnRegistro.Generos;
        }

        public string UrlImagenVideojuego { get => urlImagenVideojuego; set => urlImagenVideojuego = value; }
        public string NombreVideojuego { get => nombreVideojuego; set => nombreVideojuego= value; }
        public double NotaMediaVideojuego { get => notaMediaVideojuego; set => notaMediaVideojuego = value;}
        public double DificultadMediaVideojuego { get => dificultadMediaVideojuego; set => dificultadMediaVideojuego = value; }
        public string Generos { get => generos; set => generos = value; }
    }
}

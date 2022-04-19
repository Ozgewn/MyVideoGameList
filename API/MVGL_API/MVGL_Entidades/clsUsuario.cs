using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_Entidades
{
    public class clsUsuario
    {
        private String id;
        private String nombreUsuario;
        private int videojuegosJugados;
        private int videojuegosPlaneados;
        private int videojuegosDropeados;
        private int videojuegosEnPausa;
        private int videojuegosJugando;
        private bool esListaPrivada;

        public clsUsuario()
        {
            this.id = "";
            this.nombreUsuario = "";
            this.videojuegosJugados = 0;
            this.videojuegosPlaneados = 0;
            this.videojuegosDropeados = 0;
            this.videojuegosEnPausa = 0;
            this.videojuegosJugando = 0;
            esListaPrivada = false;

        }
        public clsUsuario(String id, String nombreUsuario, int videojuegosJugados, int videojuegosPlaneados, int videojuegosDropeados, int videojuegosEnPausa, int videojuegosJugando, bool esListaPrivada)
        {
            this.id = id;
            this.nombreUsuario = nombreUsuario;
            this.videojuegosJugados = videojuegosJugados;
            this.videojuegosPlaneados = videojuegosPlaneados;
            this.videojuegosDropeados = videojuegosDropeados;
            this.videojuegosEnPausa = videojuegosEnPausa;
            this.videojuegosJugando = videojuegosJugando;
            this.esListaPrivada = esListaPrivada;
        }

        public String Id { get => id; set => id = value; }
        public String NombreUsuario { get => nombreUsuario; set => nombreUsuario = value;}
        public int VideojuegosJugados { get => videojuegosJugados; set => videojuegosJugados = value; }
        public int VideojuegosPlaneados { get => videojuegosPlaneados; set => videojuegosPlaneados = value; }
        public int VideojuegosDropeados { get => videojuegosDropeados; set => videojuegosDropeados = value; }
        public int VideojuegosEnPausa { get => videojuegosEnPausa; set => videojuegosEnPausa = value; }
        public int VideojuegosJugando { get => videojuegosJugando; set => videojuegosJugando = value; }
        public bool EsListaPrivada { get => esListaPrivada; set => esListaPrivada = value; }
    }
}

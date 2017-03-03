

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CatandomizerServlet
 */
@WebServlet(description = "Servlet for Catandomizer", urlPatterns = { "/CatandomizerServlet" })
public class CatandomizerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RandomBoard board;
		
		if (request.getParameter("desert") != null) {
			if (request.getParameter("cluster") != null) {
				board = new RandomBoard(true, true);
			}
			else {
				board = new RandomBoard(true, false);
			}
		}
		else {
			if (request.getParameter("cluster") != null) {
				board = new RandomBoard(false, true);
			}
			else {
				board = new RandomBoard(false, false);
			}
		}
		
		Terrain[] tiles = board.getBoard();
		response.getWriter().print("{\"tiles\": [");
		for (int i = 0; i < tiles.length-1; i++) {
			response.getWriter().print("\"" + tiles[i] + "\", ");
		}
		response.getWriter().print("\"" + tiles[tiles.length-1] + "\"");
		response.getWriter().print("]}");
	}

}

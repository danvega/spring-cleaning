package dev.danvega.talks.versioning;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * The API versioning everyone wrote by hand. Reads the X-API-Version header,
 * sends version 2.0 to the /api/v2 controllers, and rejects versions we don't support.
 */
@Component
public class VersionFilter extends OncePerRequestFilter {

	private static final Set<String> SUPPORTED = Set.of("1.0", "2.0");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String version = request.getHeader("X-API-Version");
		if (version != null && !SUPPORTED.contains(version)) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), "Unsupported API version " + version);
			return;
		}
		String path = request.getRequestURI();
		if ("2.0".equals(version) && path.startsWith("/api/talks")) {
			request.getRequestDispatcher(path.replaceFirst("^/api/", "/api/v2/")).forward(request, response);
			return;
		}
		chain.doFilter(request, response);
	}

}

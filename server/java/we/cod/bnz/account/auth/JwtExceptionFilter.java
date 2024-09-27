package we.cod.bnz.account.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain filter) throws ServletException, IOException {
    try {
      filter.doFilter(req, res);
    } catch (ExpiredJwtException e) {
      setErrorResponse(res, "ACCESS_TOKEN_EXPIRED");
    } catch (MalformedJwtException e) {
      setErrorResponse(res, "INVALID JWT TOKEN");
    } catch (JwtException | SecurityException e) {
      setErrorResponse(res, "CANNOT LOGIN");
    }
  }

  public void setErrorResponse(HttpServletResponse res,
                               String msg) throws IOException {
    res.setStatus(HttpStatus.UNAUTHORIZED.value());
    res.setContentType("application/json; charset=UTF-8");
    Map<String, String> map = new HashMap<>();
    map.put("errorCode", "B001");
    map.put("msg", msg);
    res.getWriter().write(mapper.writeValueAsString(map));
  }
}

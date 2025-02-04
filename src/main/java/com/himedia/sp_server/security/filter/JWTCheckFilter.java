package com.himedia.sp_server.security.filter;

import com.google.gson.Gson;
import com.himedia.sp_server.dto.MemberDTO;
import com.himedia.sp_server.security.util.CustomJWTException;
import com.himedia.sp_server.security.util.JWTUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeaderStr = request.getHeader("Authorization");
        String accessToken = authHeaderStr.substring(7);
        try {
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            String userid = (String) claims.get("userid");
            String pwd = (String) claims.get("pwd");
            String name = (String) claims.get("name");
            String email = (String) claims.get("email");
            String phone = (String) claims.get("phone");
            String sns_id = (String) claims.get("sns_id");
            String provider = (String) claims.get("provider");
            String sns_user = (String) claims.get("sns_user");
            //Timestamp indate = (Timestamp) claims.get("indate");
            Timestamp indate = null;
            List<String> list = new ArrayList<>();
            list.add("USER");

            MemberDTO memberdto = new MemberDTO( userid, pwd, name, email, phone, indate, provider, sns_id, sns_user, list );

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(  memberdto, pwd , memberdto.getAuthorities() );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);

        } catch (CustomJWTException e) {
            log.error("JWT Check Error..............");
            log.error(e.getMessage());
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }


    }


    // 토큰없이 요청을 수락해주어야하는 요청들의 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.info("check uri.............." + path);
        
        // 요청 유알엘이 /member/login 로 시작되면 토큰 필터링 없이 요청을 수락합니다
        if( path.startsWith("/member/login") )
            return true;

        if(path.startsWith("/images/") )
            return true;

        if( path.startsWith("/member/idcheck") )
            return true;

        if( path.startsWith("/member/join") )
            return true;

        if( path.startsWith("/member/logout") )
            return true;

        if( path.startsWith("/member/refresh") )
            return true;

        return false;
    }
}

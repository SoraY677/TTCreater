package com.example.test1.AccessAuth
/*
*　Webのユーザ認証に関する定義を行うクラス
* index.htmlはログイン画面のため，すべてのユーザがアクセス可能となるが，
* 他の機能についてはログイン処理が必須となる．
* また，認証についても組織管理者と組織所属者で使用できる機能が異なるため，
* 分ける必要あり．
 */

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
@Import(HttpSessionListenerImpl::class)
class WebSecureConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var userDetailsService : UserDetailsServiceImpl

    // 静的リソース(images、css、javascript)に対するアクセスはセキュリティ設定を無視する
    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
                "/images/**",
                "/css/**",
                "/javascript/**",
                "/webjars/**")
    }

    // 認可の設定
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        //URL:"/login"でログイン認証処理を行う
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")//ログイン時処理を行う紐づけURL
                .loginPage("/login-page")//ログインページ
                .usernameParameter("user_id")//ユーザID
                .passwordParameter("password")//パスワード
                .successForwardUrl("/shiftmaker")//成功時
                .failureUrl("/login-page?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();
        /*
        //ログアウト時の処理
        http.logout()
                .logoutUrl("")
                .logoutSuccessUrl("/index");
        */

    }

    //認証処理
    @Autowired
    @Throws(Exception::class)
    override fun configure(auth:AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(BCryptPasswordEncoder());
    }
}

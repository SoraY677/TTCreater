package com.example.test1.AccessAuth

import com.example.test1.AccessAuth.DBAccesser.UserAuthRepository
import com.example.test1.ShiftInfoDBAccesser.UserInfoRepositry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession
import javax.transaction.Transactional



/**
 * UserDetailsServiceの実装クラス
 * Spring Securityでのユーザー認証に使用する
 */
@Service
@Transactional
class UserDetailsServiceImpl : UserDetailsService {

    //ユーザ認証
    @Autowired
    lateinit var userAuthRepository: UserAuthRepository
    //ユーザ情報
    @Autowired
    lateinit var userInfoRepository: UserInfoRepositry

    @Autowired
    lateinit var httpSession : HttpSession

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(loginId: String): UserDetails? {

            //HTTPセッションにユーザIDを預けておく
            httpSession.setAttribute("user_id",loginId)

            var userAuth = userAuthRepository.findPassbyId(loginId)
            // 認証を行うユーザー情報を格納する

            // ユーザー情報を取得できなかった場合
            if (userAuth == null) {
                throw UsernameNotFoundException("User not found for login id: " + loginId);
            }
            //権限のリスト
            //AdminやUserなどが存在するが、今回は利用しないのでUSERのみを仮で設定
            //権限を利用する場合は、DB上で権限テーブル、ユーザ権限テーブルを作成し管理が必要
            val grantList = ArrayList<GrantedAuthority>()
            val authority = SimpleGrantedAuthority("USER")
            grantList.add(authority)
            //rawDataのパスワードは渡すことができないので、暗号化
            val encoder = BCryptPasswordEncoder()

            var userDetails: UserDetails = User(userAuth.userId, encoder.encode(userAuth.userPassword), grantList) as UserDetails

            //ユーザの情報取得
            var userInfo = userInfoRepository.findAllbyId()
            if(userInfo != null) {
                httpSession.setAttribute("user_name", userInfo.userName)
                httpSession.setAttribute("user_status",userInfo.userStatus)
            }

            // ユーザー情報が取得できたらSpring Securityで認証できる形で戻す
            return userDetails
    }
}
import { createContext, useState } from "react";

// 사용자 인증 정보를 관리하기 위한 컨텍스트 생성
export const AuthContext = createContext();

function AuthProvider({ children }) {

  // 사용자 인증 상태 및 설정 함수 생성
  // 컨텍스트의 값으로 사용될 객체 생성
  const [auth, setAuth] = useState(localStorage.getItem("id"));
  const value = { auth, setAuth };

  return (
    // AuthContext.Provider를 사용하여 컨텍스트의 값을 제공,
    // AuthProvider 컴포넌트의 자식 요소를 렌더링
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );

}

export default AuthProvider;
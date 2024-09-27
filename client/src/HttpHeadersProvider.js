import { createContext, useState } from "react";

export const HttpHeadersContext = createContext();

function HttpHeadersProvider({ children }) {

  // 새로고침 시 App Context 사라지므로, 초기값=LocalStorage 세팅
  const [headers, setHeaders] = useState({
    "Authorization": `Bearer ${localStorage.getItem("accessToken")}`
  });

  const value = { headers, setHeaders };

  return (
    <HttpHeadersContext.Provider value={value}>
      {children}
    </HttpHeadersContext.Provider>
  );

}

export default HttpHeadersProvider;
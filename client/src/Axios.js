import authAxios from "./interceptors";

export let loginUser;

// 페이지 마운트 ---> 어스 데이터를 로그인 유저에 저장
export async function componentDidMount() {
  try {
    // auth 테스트 결과.AuthTest를 res 에 담음
    const res = (await authAxios.get("/auth/test")).data.authTest.username;
    // res의 username을 로그인유저에 담음
    loginUser = res;
    return res;
  } catch (error) {
    console.log("user not found");
  }
}
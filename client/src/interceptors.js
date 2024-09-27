import axios from "axios";

const authAxios = axios.create({
	baseUrl: `${process.env.REACT_APP_API}`,
	headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` }
})

authAxios.interceptors.request.use(
	(config) => {
		const accessToken = localStorage.getItem("accessToken");
		if (accessToken) config.headers.Authorization = `Bearer ${accessToken}`;
		return config;
	}, (error) => {
		console.log(error);
		// console.log("user not found");
	}
)

authAxios.interceptors.response.use(
	(response) => response,
	(error) => {
		if (error.response.data.errorCode === "B001") { localStorage.clear(); }
		return Promise.reject(error.response.data);
	}
)

export default authAxios;
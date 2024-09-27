import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "../../assets/mateList.scss";

// Import images
import heart from "../../assets/icons/heart.png";
import cmmnt from "../../assets/icons/alert.png";

const MateList = () => {
  const [mates, setMates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedGoal, setSelectedGoal] = useState("all");
  const [selectedType, setSelectedType] = useState("all");
  const [selectedFilters, setSelectedFilters] = useState({
    types: [],
    recruitStatus: [],
  });
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [matesPerPage] = useState(8);

  const navigate = useNavigate();

  useEffect(() => {
    fetchMates();
  }, []);

  const fetchMates = async () => {
    setLoading(true);
    try {
      const response = await axios.get(`http://localhost:8080/mate/`);
      setMates(Array.isArray(response.data) ? response.data : []);
      // console.log(response);
    } catch (error) {
      console.error("전체 리스트를 가져오지 못했습니다.", error);
      setMates([]);
      setError("전체 리스트를 가져오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const toggleFilter = (filterType, filterValue) => {
    setSelectedFilters((prevFilters) => {
      const newFilters = { ...prevFilters };
      if (newFilters[filterType].includes(filterValue)) {
        newFilters[filterType] = newFilters[filterType].filter(
          (val) => val !== filterValue
        );
      } else {
        newFilters[filterType].push(filterValue);
      }
      return newFilters;
    });
  };

  const handleSearchInputChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleSearch = () => {
    setCurrentPage(1); // 검색 시 첫 페이지로 이동
  };

  const filteredMates = mates.filter((mate) => {
    const matchesGoal =
      selectedGoal === "all" || (mate.tag && mate.tag.goal === selectedGoal);
    const matchesType =
      selectedType === "all" || (mate.tag && mate.tag.space === selectedType);
    const matchesRecruit =
      selectedFilters.recruitStatus.length === 0 ||
      (mate.tag &&
        ((selectedFilters.recruitStatus.includes("recruit") &&
          mate.tag.recruit) ||
          (selectedFilters.recruitStatus.includes("complete") &&
            !mate.tag.recruit)));
    const matchesSearchTerm = mate.title
      .toLowerCase()
      .includes(searchTerm.toLowerCase());
    return matchesGoal && matchesType && matchesRecruit && matchesSearchTerm;
  });

  const indexOfLastMate = currentPage * matesPerPage;
  const indexOfFirstMate = indexOfLastMate - matesPerPage;
  const currentMates = filteredMates.slice(indexOfFirstMate, indexOfLastMate);

  const totalPages = Math.ceil(filteredMates.length / matesPerPage);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  return (
    <div className="mate_list_con">
      <h4 className="mate_title">Mate List</h4>
      <div className="mate_top_abs">
        <button
          onClick={() => navigate("/mate/create")}
          className="mate_list_button"
        >
          작성하기
        </button>
        <div className="mate_search_bar">
          <input
            type="text"
            value={searchTerm}
            onChange={handleSearchInputChange}
            placeholder="검색어를 입력하세요"
          />
          <button onClick={handleSearch}>검색</button>
        </div>
      </div>
      <div>
        <button
          onClick={() => setSelectedGoal("all")}
          className={`mate_cate ${selectedGoal === "all" ? "active" : ""}`}
        >
          전체
        </button>
        <button
          onClick={() => setSelectedGoal("스터디")}
          className={`mate_cate ${selectedGoal === "스터디" ? "active" : ""}`}
        >
          스터디
        </button>
        <button
          onClick={() => setSelectedGoal("프로젝트")}
          className={`mate_cate ${selectedGoal === "프로젝트" ? "active" : ""}`}
        >
          프로젝트
        </button>
      </div>
      <div>
        <button
          onClick={() => setSelectedType("all")}
          className={`mate_space ${
            selectedType === "all" ? "space_active" : ""
          }`}
        >
          전체
        </button>
        <button
          onClick={() => setSelectedType("ONLINE")}
          className={`mate_space ${
            selectedType === "ONLINE" ? "space_active" : ""
          }`}
        >
          온라인
        </button>
        <button
          onClick={() => setSelectedType("OFFLINE")}
          className={`mate_space ${
            selectedType === "OFFLINE" ? "space_active" : ""
          }`}
        >
          오프라인
        </button>
        <button
          onClick={() => toggleFilter("recruitStatus", "recruit")}
          className={`mate_recruit ${
            selectedFilters.recruitStatus.includes("recruit")
              ? "recruit_active"
              : ""
          }`}
        >
          모집중
        </button>
        <button
          onClick={() => toggleFilter("recruitStatus", "complete")}
          className={`mate_recruit ${
            selectedFilters.recruitStatus.includes("complete")
              ? "recruit_active"
              : ""
          }`}
        >
          모집완료
        </button>
      </div>
      {loading ? (
        <p>로딩 중...</p>
      ) : error ? (
        <p>{error}</p>
      ) : (
        <>
          <div className="mate_list_ul">
            {currentMates.length > 0 ? (
              currentMates.map((mate) => (
                <div key={mate.id} className="mate_list_li">
                  <Link to={`/mate/detail/${mate.id}`}>
                    <span className="mate_li_recruit">
                      {mate.tag && mate.tag.recruit ? "모집중" : "모집완료"}
                    </span>
                    <h4 className="mate_title">{mate.title}</h4>
                    <p>{mate.author || "Unknown author"}</p>
                    <ul className="mate_emotion">
                      <li>
                        <img src={heart} alt="heart" /> {mate.likes}
                      </li>
                      <li>
                        <img src={cmmnt} alt="cmmnt" /> {mate.commentCnt}
                      </li>
                      <li>Hits: {mate.hits}</li>
                      <li>{new Date(mate.create_date).toLocaleDateString()}</li>
                    </ul>
                    <div className="tag_list">
                      {mate.tag && (
                        <div className="detail_tag_flex">
                          <ul className="detail_tag_wrap">
                            <li className="goal">
                              <span>Goal: {mate.tag.goal}</span>
                            </li>
                            <li className="space">
                              <span>Space: {mate.tag.space}</span>
                            </li>
                            <li className="program">
                              {Array.from(mate.tag.locas).map(
                                (loca, locaIndex) => (
                                  <span key={locaIndex}>{loca}</span>
                                )
                              )}
                            </li>
                            <li className="part">
                              {Array.from(mate.tag.parts).map(
                                (part, partIndex) => (
                                  <span key={partIndex}>{part}</span>
                                )
                              )}
                            </li>
                            <li className="language">
                              {Array.from(mate.tag.langs).map(
                                (lang, langIndex) => (
                                  <span key={langIndex}>{lang}</span>
                                )
                              )}
                            </li>
                          </ul>
                        </div>
                      )}
                    </div>
                  </Link>
                </div>
              ))
            ) : (
              <p>게시글이 존재하지 않습니다.</p>
            )}
          </div>
          <div className="mate_pagination">
            {Array.from({ length: totalPages }, (_, index) => index + 1).map(
              (page) => (
                <button
                  key={page}
                  onClick={() => handlePageChange(page)}
                  className={`mate_page_item ${
                    currentPage === page ? "mate_active" : ""
                  }`}
                >
                  {page}
                </button>
              )
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default MateList;

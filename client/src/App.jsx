import { Route, BrowserRouter, Routes } from 'react-router-dom';

// Basic
import Footer from './layouts/Footer'
import Header from './layouts/Header';
import Main from './pages/main/Main'
import Admin from './pages/Admin'

// Talk
import Talk from './pages/talk/Talk'
import AddTalk from './pages/talk/AddTalk'
import TalkDetail from './pages/talk/TalkDetail'

// Mate
import Mate from './pages/mate/MateList'
import MateForm from "./pages/mate/MateForm";
import MateDetail from "./pages/mate/MateCmmnt";
// import MateList from "./pages/mate/MateList";

// Today
import Today from './pages/today/bbs/Today'
import TodayWrite from './pages/today/bbs/TodayWrite'
import TodayUpdate from './pages/today/bbs/TodayUpdate'
import TodayDetail from './pages/today/bbs/TodayDetail'
// import Plan from './pages/plan/Plan'
import Plan from './pages/plan/Calendar';

// Login, Join
import Login from './pages/account/Login'
import Join1 from './pages/account/Join_1'
import Join2 from './pages/account/Join_2'

// My
import My from './pages/my/main/My'
import MyTeam from './pages/my/team/Detail'
import MyBoard from './pages/my/board/Detail';
import MyBookmark from './pages/my/bookmark/Detail';
import MyUpdate from './pages/my/profile/Update';

//== header, auth ==//
import AuthProvider from './AuthProvider';
import HttpHeadersProvider from './HttpHeadersProvider';



function App() {

  return (
    <BrowserRouter>
      <AuthProvider>
        <HttpHeadersProvider>
          <Header />

          <div id='main'>
            <Routes>
              <Route path="/" element={<Main />} />
              <Route path="/admin" element={<Admin />} />

              <Route path="/mate" element={<Mate />} />
              <Route path="/mate/create" element={<MateForm />} />
              <Route path="/mate/detail/:id" element={<MateDetail />} />

              <Route path="/today" element={<Today />} />
              <Route path="/today/add" element={<TodayWrite />} />
              <Route path="/todayupdate" element={<TodayUpdate />} />
              <Route path="/today/:todayId" element={<TodayDetail />} />

              <Route path="/team" element={<Talk kind={`bnzTeam`} />} />
              <Route path="/talk" element={<Talk kind={`bnzTalk`} />} />
              <Route path="/team/:id" element={<TalkDetail kind={`bnzTeam`} />} />
              <Route path="/talk/:id" element={<TalkDetail kind={`bnzTalk`} />} />
              <Route path="/team/add" element={<AddTalk />} />

              <Route path="/plan" element={<Plan />} />

              <Route path="/account/login" element={<Login />} />
              <Route path="/account/join/1" element={<Join1 />} />
              <Route path="/account/join/2" element={<Join2 />} />

              <Route path="/my" element={<My />} />
              <Route path="/my/profile" element={<MyUpdate />} />
              <Route path="/my/team" element={<MyTeam />} />
              <Route path="/my/board" element={<MyBoard />} />
              <Route path="/my/bookmark" element={<MyBookmark />} />
            </Routes>
          </div>

          <Footer />
        </HttpHeadersProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;

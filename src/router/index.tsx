import { createBrowserRouter } from 'react-router-dom';
import LandingPage from '../pages/Landing';
import { LoginPage } from '../pages/Login';
import { SignUpPage } from '../pages/SignUp';
import DashboardPage from '../pages/Dashboard';
import CalendarPage from '../pages/Calendar';
import { MyPage } from '../pages/MyPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <LandingPage />,
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/signup',
    element: <SignUpPage />,
  },
  {
    path: '/dashboard',
    element: <DashboardPage />,
  },
  {
    path: '/calendar',
    element: <CalendarPage />,
  },
  {
    path: '/mypage',
    element: <MyPage />,
  },
]);

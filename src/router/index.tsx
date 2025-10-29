import { createBrowserRouter } from 'react-router-dom';
import App from '../App';
import { LoginPage } from '../components/LoginPage';
import { SignUpPage } from '../components/SignUpPage';
import { DashboardPage } from '../components/DashboardPage';
import { CalendarPage } from '../components/CalendarPage';
import { MyPage } from '../components/MyPage';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
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

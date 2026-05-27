import { HttpInterceptorFn } from '@angular/common/http';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {

  const token = localStorage.getItem('token');

  const isPublicRequest = req.url.includes('/login') || req.url.includes('/signup');

  if (token && !isPublicRequest) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`      
      }
    });
    return next(clonedReq);
  }
  return next(req);
};
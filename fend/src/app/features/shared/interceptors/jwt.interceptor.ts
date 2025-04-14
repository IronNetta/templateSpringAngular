import { HttpInterceptorFn } from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../../auth/services/auth-service.service';
import {UserTokenDto} from '../../auth/models/user-token-dto.model';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const currentUser = authService.currentUser();

  console.log('Interceptor - Current User:', currentUser);

  if (currentUser && currentUser.token) {
    console.log('Interceptor - Token:', currentUser.token);

    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${currentUser.token}`
      }
    });

    return next(clonedRequest);
  }

  return next(req);
};

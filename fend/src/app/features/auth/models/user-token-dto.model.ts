export interface UserTokenDto {
  token: string;
  user: UserSessionDto;
}

export interface UserSessionDto {
  id: number;
  userName: string;
  role: string;
}

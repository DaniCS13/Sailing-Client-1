package cat.institutmarianao.sailing.ws.model.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.Admin;
import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.dto.AdminDto;
import cat.institutmarianao.sailing.ws.model.dto.ClientDto;
import cat.institutmarianao.sailing.ws.model.dto.UserDto;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

	@Override
	public User convert(UserDto userDto) {
		if (userDto instanceof AdminDto adminDto) {
			Admin admin = new Admin();
			BeanUtils.copyProperties(adminDto, admin);
			return admin;
		}
		if (userDto instanceof ClientDto clientDto) {
			Client client = new Client();
			BeanUtils.copyProperties(clientDto, client);
			return client;
		}
		return null;
	}
}

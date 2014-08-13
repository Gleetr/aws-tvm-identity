/*
 * Copyright 2014 Gleetr SAS or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amazonaws.tvm.services.dao;

import com.amazonaws.tvm.domain.User;
import com.amazonaws.tvm.domain.UserCreationRequest;

import java.util.List;

/**
 * Store and manages users.
 */
public interface UserService {

    public List<User> list();

    public User findById(String id);

    public User findByName(String name);

    public void deleteById(String id);

    public void add(UserCreationRequest user, String endpoint);

    public Long count();

}
